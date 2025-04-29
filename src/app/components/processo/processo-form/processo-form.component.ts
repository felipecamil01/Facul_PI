import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink, RouterModule } from '@angular/router';
import { ProcessoService } from '../../../services/processo.service';
import { ClienteService } from '../../../services/cliente.service';
import { Cliente } from '../../../models/cliente.model';
import { LoginService } from '../../../auth/login.service';
import Swal from 'sweetalert2';
import { ClienteDTO } from '../../../models/ClienteDTO';

@Component({
  selector: 'app-processo-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,RouterLink,RouterModule],
  templateUrl: './processo-form.component.html',
  styleUrls: ['./processo-form.component.scss']
})
export class ProcessoFormComponent implements OnInit {
  loginService = inject(LoginService);
  processoForm: FormGroup;
  clientes: ClienteDTO[] = [];
  modoEdicao = false;
  registroSelecionadoId?: number;

  private fb = inject(FormBuilder);
  private processoService = inject(ProcessoService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private clienteService = inject(ClienteService);

  constructor() {
    this.processoForm = this.fb.group({
      id: [null],
      tipoCliente: ['', Validators.required],
      areaAtuacao: ['', Validators.required],
      numeroProcesso: ['', [Validators.required, Validators.minLength(3)]],
      comarca: ['', Validators.required],
      dataInicio: ['', Validators.required],
      descricao: ['', Validators.required],
      andamento: [''],
      situacaoAtual: ['', Validators.required],
      cliente: [null, Validators.required],
      prazosImportantes: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.carregaCliente();

    if (this.route.snapshot.paramMap.get('id')) {
      const id = Number(this.route.snapshot.paramMap.get('id'));
      this.processoService.findById(id).subscribe({
        next: processo => this.editarRegistro(processo),
        error: err => console.log(err)
      });
    }
  }

  get prazosImportantes() {
    return this.processoForm.get('prazosImportantes') as FormArray;
  }

  carregaCliente(): void {
    this.clienteService.findAll().subscribe(
      data => this.clientes = data,
      error => console.error('Erro ao buscar clientes', error)
    );
  }

  editarRegistro(processo: any): void {
    this.modoEdicao = true;
    this.registroSelecionadoId = processo.id;
    this.processoForm.patchValue({
      tipoCliente: processo.tipoCliente,
      areaAtuacao: processo.areaAtuacao,
      numeroProcesso: processo.numeroProcesso,
      comarca: processo.comarca,
      dataInicio: processo.dataInicio,
      descricao: processo.descricao,
      andamento: processo.andamento,
      situacaoAtual: processo.situacaoAtual,
      cliente: processo.cliente.id
    });
    processo.prazosImportantes?.forEach((prazo: any) => {
      this.adicionarPrazo(prazo);
    });
  }

  adicionarPrazo(prazo: Date | null): void {
    const prazoControl = this.fb.control(prazo || '');
    this.prazosImportantes.push(prazoControl);
  }

  removerPrazo(index: number): void {
    this.prazosImportantes.removeAt(index);
  }

  onSubmit(): void {
    if (this.processoForm.valid) {
      this.clienteService.findById(this.processoForm.value.cliente).subscribe({
        next: (cliente) => {
          const dadosProcesso = {
            ...this.processoForm.value,
            cliente: cliente
          };
  
          const operacao = dadosProcesso.id ?
            this.processoService.update(dadosProcesso.id, dadosProcesso) :
            this.processoService.save(dadosProcesso);
  
          operacao.subscribe({
            next: () => {
              Swal.fire({
                icon: 'success',
                title: dadosProcesso.id ? 'Processo atualizado!' : 'Processo criado!',
                showConfirmButton: false,
                timer: 1500
              });
              if(this.loginService.hasPermission("ADMIN")){
                this.router.navigate(['admin/processo']);
              }else{
                this.router.navigate(['user/processo']);
              }
            },
            error: (erro) => {
              console.error('Erro ao salvar processo', erro);
              Swal.fire('Erro', 'Não foi possível salvar o processo', 'error');
            }
          });
        },
        error: () => {
          Swal.fire({
            title: 'Erro ao buscar cliente',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      });
    } else {
      this.markFormGroupTouched(this.processoForm);
    }
  }

  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  campoInvalido(nomeCampo: string): boolean {
    const campo = this.processoForm.get(nomeCampo);
    return !!(campo && campo.invalid && (campo.dirty || campo.touched));
  }

  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  }
}
