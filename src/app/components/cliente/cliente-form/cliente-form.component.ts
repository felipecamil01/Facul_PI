import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ClienteService } from '../../../services/cliente.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { ViaCepService } from '../../../services/viacep.service';
import { KeycloakService } from '../../../auth/keycloak-service';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterModule],
  templateUrl: './cliente-form.component.html',
  styleUrls: ['./cliente-form.component.scss']
})
export class ClienteFormComponent implements OnInit {
  keycloakService = inject(KeycloakService);
  clienteForm!: FormGroup;
  idCliente!: number;
  modoEdicao: boolean = false;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private route: Router,
    private viaCepService: ViaCepService,
    private activatedRoute: ActivatedRoute
  ) {
    this.clienteForm = this.fb.group({
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      cpf: ['', [Validators.required]],
      rg: ['', [Validators.required]],
      profissao: ['', Validators.required],
      telefone: ['', Validators.required],
      dataNascimento: ['', Validators.required],
      estadoCivil: ['', Validators.required],
      endereco: this.fb.group({
        logradouro: [''],
        numero: [''],
        complemento: [''],
        bairro: [''],
        cidade: [''],
        uf: [''],
        cep: ['']
      })
    });
  }

  ngOnInit(): void {
    const clienteId = this.activatedRoute.snapshot.paramMap.get('id');
    if (clienteId) {
      this.idCliente = Number(clienteId);
      this.modoEdicao = true;
      this.clienteService.findById(this.idCliente).subscribe(cliente => {
        this.clienteForm.patchValue({
          nome: cliente.nome,
          email: cliente.email,
          cpf: cliente.cpf,
          rg: cliente.rg,
          profissao: cliente.profissao,
          telefone: cliente.telefone,
          dataNascimento: cliente.dataNascimento,
          estadoCivil: cliente.estadoCivil,
          endereco: {
            logradouro: cliente.endereco.logradouro,
            numero: cliente.endereco.numero,
            complemento: cliente.endereco.complemento,
            bairro: cliente.endereco.bairro,
            cidade: cliente.endereco.cidade,
            uf: cliente.endereco.uf,
            cep: cliente.endereco.cep
          }
        });
      });
    }
  }

  onSubmit(): void {
    if (this.clienteForm.valid) {
      const clienteData = this.clienteForm.value;
      const rota = this.getRoute('cliente');

      if (this.idCliente) {
        this.clienteService.update(this.idCliente, clienteData).subscribe({
          next: () => {
            Swal.fire({ title: 'Atualizado com sucesso', icon: 'success', confirmButtonText: 'OK' });
            this.route.navigate([rota]);
          },
          error: () => {
            Swal.fire({ title: 'Erro ao Atualizar', icon: 'error', confirmButtonText: 'OK' });
          }
        });
      } else {
        this.clienteService.save(clienteData).subscribe({
          next: () => {
            Swal.fire({ title: 'Cadastrado com sucesso', icon: 'success', confirmButtonText: 'OK' });
            this.route.navigate([rota]);
          },
          error: () => {
            Swal.fire({ title: 'Erro ao salvar', icon: 'error', confirmButtonText: 'OK' });
          }
        });
      }
    }
  }

  buscarEndereco(): void {
    const cep = this.clienteForm.get('endereco.cep')?.value;
    if (cep) {
      this.viaCepService.buscarEndereco(cep).subscribe(
        (data) => {
          if (data) {
            this.clienteForm.patchValue({
              endereco: {
                logradouro: data.logradouro,
                bairro: data.bairro,
                cidade: data.localidade,
                uf: data.uf
              }
            });
            const paisField = document.getElementById('pais') as HTMLInputElement;
            if (paisField) paisField.value = 'Brasil';
          } else {
            Swal.fire({ title: 'Erro', text: 'Endereço não encontrado para o CEP informado.', icon: 'error', confirmButtonText: 'OK' });
          }
        },
        () => {
          Swal.fire({ title: 'Erro', text: 'Erro ao buscar endereço. Verifique o CEP e tente novamente.', icon: 'error', confirmButtonText: 'OK' });
        }
      );
    } else {
      Swal.fire({ title: 'Atenção', text: 'Por favor, insira um CEP válido.', icon: 'warning', confirmButtonText: 'OK' });
    }
  }

  getRoute(path: string): string {
    return this.keycloakService.getProfile?.role === 'ADMIN' ? `/admin/${path}` : `/user/${path}`;
  }
}