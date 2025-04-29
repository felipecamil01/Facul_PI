import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ClienteService } from '../../../services/cliente.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, NgForm, FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { ViaCepService } from '../../../services/viacep.service';
import { LoginService } from '../../../auth/login.service';
import { OrgaoExpedidor } from '../../../models/EmissorEmissor.enum';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [ReactiveFormsModule,RouterModule,FormsModule],
  templateUrl: './cliente-form.component.html',
  styleUrls: ['./cliente-form.component.scss']
})
export class ClienteFormComponent implements OnInit {
  
  loginService = inject(LoginService);
  orgaosAgrupados: { label: string; orgaos: { sigla: string; descricao: string; }[] }[] = [];
  clienteForm!: FormGroup;
  idCliente!: number;
  modoEdicao: boolean = false;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private route: Router,
    private viaCepService: ViaCepService,
    private activatedRoute: ActivatedRoute,
  ) {
    this.clienteForm = this.fb.group({
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      cpf: ['', [Validators.required]],
      orgaoExpedidor:[''],
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
      this.orgaosAgrupados = this.gerarOrgaosAgrupados();
      this.idCliente = Number(clienteId);
      this.modoEdicao = true;
      this.clienteService.findById(this.idCliente).subscribe(cliente => {
        this.clienteForm.patchValue({
          nome: cliente.nome,
          email: cliente.email,
          cpf: cliente.cpf,
          orgaoExpedidor:cliente.orgaoEmissor,
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
      if (this.idCliente) {
        this.clienteService.update(this.idCliente, clienteData).subscribe({
          next: () => {
            Swal.fire({
              title: 'Atualizado com sucesso',
              icon: 'success',
              confirmButtonText: 'OK'
            });
            if(this.loginService.hasPermission("ADMIN")){
              this.route.navigate(['admin/cliente']);
            }else{
              this.route.navigate(['user/cliente']);
            }
          },
          error: (error) => {
            Swal.fire({
              title: 'Erro ao Atualizar',
              icon: 'error',
              confirmButtonText: 'OK'
            });
          }
        });
      } else {
        this.clienteService.save(clienteData).subscribe({
          next: () => {
            Swal.fire({
              title: 'Cadastrado com sucesso',
              icon: 'success',
              confirmButtonText: 'OK'
            });
            if(this.loginService.hasPermission("ADMIN")){
              this.route.navigate(['admin/cliente']);
            }else{
              this.route.navigate(['user/cliente']);
            }
          },
          error: (error) => {
            Swal.fire({
              title: 'Erro ao salvar',
              icon: 'error',
              confirmButtonText: 'OK'
            });
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
            if (paisField) {
              paisField.value = 'Brasil';
            }

          } else {
            Swal.fire({
              title: 'Erro',
              text: 'Endereço não encontrado para o CEP informado.',
              icon: 'error',
              confirmButtonText: 'OK'
            });
          }
        },
        (error) => {
          Swal.fire({
            title: 'Erro',
            text: 'Erro ao buscar endereço. Verifique o CEP e tente novamente.',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      );
    } else {
      Swal.fire({
        title: 'Atenção',
        text: 'Por favor, insira um CEP válido.',
        icon: 'warning',
        confirmButtonText: 'OK'
      });
    }
  }

  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  }
  private gerarOrgaosAgrupados() {
    return [
      {
        label: 'Órgãos Federais',
        orgaos: [
          { sigla: 'SSP', descricao: 'Secretaria de Segurança Pública' },
          { sigla: 'IFP', descricao: 'Instituto Félix Pacheco' },
          { sigla: 'DIREX', descricao: 'Diretoria Executiva' },
          { sigla: 'DPF', descricao: 'Departamento de Polícia Federal' },
          { sigla: 'DPRF', descricao: 'Departamento de Polícia Rodoviária Federal' },
          { sigla: 'MAE', descricao: 'Ministério da Aeronáutica' },
          { sigla: 'MEX', descricao: 'Ministério do Exército' },
          { sigla: 'MMA', descricao: 'Ministério da Marinha' },
          { sigla: 'MTPS', descricao: 'Ministério do Trabalho e Previdência Social' },
          { sigla: 'CNT', descricao: 'Carteira Nacional de Habilitação' },
          { sigla: 'CTPS', descricao: 'Carteira de Trabalho e Previdência Social' },
          { sigla: 'CRM', descricao: 'Conselho Regional de Medicina' },
          { sigla: 'OAB', descricao: 'Ordem dos Advogados do Brasil' },
        ]
      },
      {
        label: 'Órgãos Estaduais',
        orgaos: [
          { sigla: 'SSP-AC', descricao: 'SSP - Acre' },
          { sigla: 'SSP-AL', descricao: 'SSP - Alagoas' },
          { sigla: 'SSP-AP', descricao: 'SSP - Amapá' },
          { sigla: 'SSP-AM', descricao: 'SSP - Amazonas' },
          { sigla: 'SSP-BA', descricao: 'SSP - Bahia' },
          { sigla: 'SSP-CE', descricao: 'SSP - Ceará' },
          { sigla: 'SSP-DF', descricao: 'SSP - Distrito Federal' },
          { sigla: 'SSP-ES', descricao: 'SSP - Espírito Santo' },
          { sigla: 'SSP-GO', descricao: 'SSP - Goiás' },
          { sigla: 'SSP-MA', descricao: 'SSP - Maranhão' },
          { sigla: 'SSP-MT', descricao: 'SSP - Mato Grosso' },
          { sigla: 'SSP-MS', descricao: 'SSP - Mato Grosso do Sul' },
          { sigla: 'SSP-MG', descricao: 'SSP - Minas Gerais' },
          { sigla: 'SSP-PA', descricao: 'SSP - Pará' },
          { sigla: 'SSP-PB', descricao: 'SSP - Paraíba' },
          { sigla: 'SSP-PR', descricao: 'SSP - Paraná' },
          { sigla: 'SSP-PE', descricao: 'SSP - Pernambuco' },
          { sigla: 'SSP-PI', descricao: 'SSP - Piauí' },
          { sigla: 'SSP-RJ', descricao: 'SSP - Rio de Janeiro' },
          { sigla: 'SSP-RN', descricao: 'SSP - Rio Grande do Norte' },
          { sigla: 'SSP-RS', descricao: 'SSP - Rio Grande do Sul' },
          { sigla: 'SSP-RO', descricao: 'SSP - Rondônia' },
          { sigla: 'SSP-RR', descricao: 'SSP - Roraima' },
          { sigla: 'SSP-SC', descricao: 'SSP - Santa Catarina' },
          { sigla: 'SSP-SP', descricao: 'SSP - São Paulo' },
          { sigla: 'SSP-SE', descricao: 'SSP - Sergipe' },
          { sigla: 'SSP-TO', descricao: 'SSP - Tocantins' },
        ]
      },
      {
        label: 'Outros',
        orgaos: [
          { sigla: 'Outros', descricao: 'Outros' }
        ]
      }
    ];
  }
  
}
