import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, registerLocaleData } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Component, inject, OnInit } from '@angular/core';
import localePt from '@angular/common/locales/pt';
import Swal from 'sweetalert2';
import { ClienteService } from '../../../services/cliente.service';
import { DespesaService } from '../../../services/despesa.service';
import { Despesa } from '../../../models/despesa.model';
import { LoginService } from '../../../auth/login.service';


registerLocaleData(localePt, 'pt-BR');

@Component({
  selector: 'app-despesa',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,RouterModule],
  templateUrl: './despesa-form.component.html',
  styleUrls: ['./despesa-form.component.scss']
})

export class DespesaFormComponent implements OnInit {
  loginService = inject(LoginService);
  registros: Despesa[] = [];
  form: FormGroup;
  modoEdicao = false;
  registroSelecionadoId?: number;
  selectedClienteId: number = 0;
  clientes: any[] = [];

  statusOptions = ['PENDENTE', 'PAGO', 'ATRASADO', 'ESTORNADO'];
  formasPagamento = ['PIX', 'CARTÃO', 'DINHEIRO', 'TRANSFERÊNCIA'];
  categoria: string[] = [];
  outroSelect = false;

  constructor(
    private route: ActivatedRoute,
    private router:Router,
    private despesaService: DespesaService,
    private fb: FormBuilder,
    private clienteService: ClienteService
  ) {
    this.form = this.fb.group({
      honorario: ['', Validators.required],
      categoriaDespesa: ['', Validators.required],
      outraCategoria: [''], 
      formaPagamento: ['', Validators.required],
      statusPagamento: ['', Validators.required],
      dataVencimento: ['', Validators.required],
      observacao: [''],
      despesasAdicionais: [''],
      clienteId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.carregarRegistros();
    this.carregaCliente();
    this.carregarCategorias();

    if (this.route.snapshot.paramMap.get('id')) {
      const id = Number(this.route.snapshot.paramMap.get('id'));
      this.despesaService.findById(id).subscribe({
        next: despesa => {
          this.editarRegistro(despesa);
        },
        error: err => {
          console.log(err);
        }
      });
    }
  }

  carregarRegistros(): void {
    this.despesaService.findAll().subscribe({
      next: (data) => {
        this.registros = data;
      },
      error: (error) => {
        console.error('Erro ao carregar registros:', error);
      }
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.clienteService.findById(this.form.value.clienteId).subscribe({
        next: (cliente) => {
          if (this.form.get('categoriaDespesa')?.value === 'OUTROS') {
            const outraCategoria = this.form.get('outraCategoria')?.value;
            this.form.get('categoriaDespesa')?.setValue(outraCategoria);
          }
          const dadosParaSalvar = {
            ...this.form.value,
            categoria: this.form.value.outraCategoria || this.form.value.categoriaDespesa,
            honorario: this.removerFormatacaoMoeda(this.form.value.honorario),
            despesasAdicionais: this.removerFormatacaoMoeda(this.form.value.despesasAdicionais),
            cliente: cliente
          };

          if (this.modoEdicao && this.registroSelecionadoId) {
            this.despesaService.update(this.registroSelecionadoId, dadosParaSalvar).subscribe({
              next: () => {
                Swal.fire({
                  title: 'Atualizado com sucesso',
                  icon: 'success',
                  confirmButtonText: 'OK'
                });
                this.carregarRegistros();
                this.limparFormulario();
                if(this.loginService.hasPermission("ADMIN")){
                  this.router.navigate(['admin/despesa']);
                }else{
                  this.router.navigate(['user/despesa']);
                }
                
              },
              error: () =>
                Swal.fire({
                  title: 'Erro ao atualizar',
                  icon: 'error',
                  confirmButtonText: 'OK'
                })
            });
          } else {
            this.despesaService.save(dadosParaSalvar).subscribe({
              next: () => {
                Swal.fire({
                  title: 'Cadastrado com sucesso',
                  icon: 'success',
                  confirmButtonText: 'OK'
                });
                this.carregarRegistros();
                this.limparFormulario();
                if(this.loginService.hasPermission("ADMIN")){
                  this.router.navigate(['admin/despesa']);
                }else{
                  this.router.navigate(['user/despesa']);
                }
                
              },
              error: () =>
                Swal.fire({
                  title: 'Erro ao salvar',
                  icon: 'error',
                  confirmButtonText: 'OK'
                })
            });
          }
        },
        error: () => {
          Swal.fire({
            title: 'Erro ao buscar cliente',
            icon: 'error',
            confirmButtonText: 'OK'
          });
        }
      });
    }
  }

  editarRegistro(registro: Despesa): void {
    this.modoEdicao = true;
    this.registroSelecionadoId = registro.id;
    this.form.patchValue({
      honorario: registro.honorario,
      formaPagamento: registro.formaPagamento,
      statusPagamento: registro.statusPagamento,
      categoriaDespesa: registro.categoriaDespesa,
      dataVencimento: registro.dataVencimento,
      despesasAdicionais: registro.despesasAdicionais,
      observacao: registro.observacao,
      clienteId: registro.cliente.id
    });
  }


  excluirRegistro(id: number): void {
    Swal.fire({
      title: 'Quer deletar este registro?',
      icon: 'warning',
      showConfirmButton: true,
      showDenyButton: true,
      confirmButtonText: 'Sim',
      denyButtonText: 'Não',
    }).then((result) => {
      if (result.isConfirmed) {
        this.despesaService.delete(id).subscribe({
          next: () => {
            Swal.fire({
              title: 'Deletado com sucesso',
              icon: 'success',
              confirmButtonText: 'OK'
            });
            this.carregarRegistros();
          },
          error: () => {
            Swal.fire({
              title: 'Erro ao deletar',
              icon: 'error',
              confirmButtonText: 'OK'
            });
          }
        });
      }
    });
  }

  limparFormulario(): void {
    this.form.reset();
    this.modoEdicao = false;
    this.registroSelecionadoId = undefined;
  }

  corDoStatus(status: string): string {
    switch (status) {
      case 'PENDENTE':
        return 'badge bg-warning';
      case 'PAGO':
        return 'badge bg-success';
      case 'ATRASADO':
        return 'badge bg-danger';
      case 'ESTORNADO':
        return 'badge bg-dark';
      default:
        return '';
    }
  }

  formatCurrency(event: Event): void {
    const input = event.target as HTMLInputElement;
    let value = input.value;

    value = value.replace(/\D/g, '');

    if (value === '') {
      value = '0';
    }

    const numericValue = parseInt(value) / 100;

    const formattedValue = new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(numericValue);

    input.value = formattedValue;

    const fieldName = input.getAttribute('formControlName');
    if (fieldName) {
      this.form.get(fieldName)?.setValue(formattedValue, { emitEvent: false });
    }
  }

  private removerFormatacaoMoeda(valor: string | number): number {
    if (valor == null) {
      return 0;
    }

    if (typeof valor === 'number') {
      return valor;
    }

    const valorLimpo = valor.replace(/[R$\s.]/g, '').replace(',', '.');
    const valorNumerico = parseFloat(valorLimpo);

    return isNaN(valorNumerico) ? 0 : valorNumerico;
  }

  carregaCliente() {
    this.clienteService.findAll().subscribe(
      (data) => (this.clientes = data),
      (error) => console.error('Erro ao buscar clientes', error)
    );
  }

  carregarCategorias(): void {
    this.despesaService.getCategoriasMaisUsadas().subscribe((data: string[]) => {
      this.categoria = data;
    });
  }
  onCategoriaChange(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    this.outroSelect = selectElement.value === 'OUTROS';
    if (!this.outroSelect) {
      this.form.get('outraCategoria')?.setValue('');
    }
  }

  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  }
}