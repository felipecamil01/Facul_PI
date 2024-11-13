import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, registerLocaleData } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import Swal from 'sweetalert2';
import localePt from '@angular/common/locales/pt';
import { ClienteService } from '../../services/cliente.service';
import { DespesaService } from '../../services/despesa.service';
import { Despesa } from '../../models/despesa.model';
import { ActivatedRoute } from '@angular/router';

registerLocaleData(localePt, 'pt-BR');

@Component({
  selector: 'app-financeiro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './despesa-save.component.html',
  styleUrls: ['./despesa-save.component.scss']
})

export class DespesaSaveComponent implements OnInit {
  registros: Despesa[] = [];
  form: FormGroup;
  modoEdicao = false;
  registroSelecionadoId?: number;
  selectedClienteId:number=0;
  clientes: any[] = [];

  statusOptions = ['PENDENTE', 'PAGO', 'ATRASADO', 'ESTORNADO'];
  formasPagamento = ['PIX', 'CARTÃO', 'DINHEIRO', 'TRANSFERÊNCIA'];
  categoria = ['TRANSPORTE', 'ALIMENTAÇÃO', 'ESCRITÓRIO', 'CAPACITAÇÃO', 'PUBLICIDADE', 'LICENÇAS'];

  constructor(
    private route:ActivatedRoute,
    private despesaService: DespesaService,
    private fb: FormBuilder,
    private clienteService:ClienteService
  ) {
    this.form = this.fb.group({
      honorario: ['', [Validators.required]],
      formaPagamento: ['', [Validators.required]],
      statusPagamento: ['', [Validators.required]],
      categoriaDespesa: ['', [Validators.required]],
      dataVencimento: ['', [Validators.required]],
      despesasAdicionais: ['', [Validators.required]],
      clienteId:['',Validators.required]
    });
  }

  ngOnInit(): void {
    this.carregarRegistros();
    this.carregaCliente();

     if(this.route.snapshot.paramMap.get('id')){
        const id = Number(this.route.snapshot.paramMap.get('id'));
        this.despesaService.findById(id).subscribe({
          next: despesa => {
            this.editarRegistro(despesa)
            console.log(despesa);
          },
          error: err =>{
            console.log(err)
          }
        }
        )
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


      if (this.modoEdicao && this.registroSelecionadoId) {
        const dadosParaSalvar = {
          ...this.form.value,
          honorario: this.removerFormatacaoMoeda(this.form.value.honorario),
        despesasAdicionais: this.removerFormatacaoMoeda(this.form.value.despesasAdicionais),
        };
        this.despesaService.update(this.registroSelecionadoId, dadosParaSalvar).subscribe({
          next: () => {
            Swal.fire({
              title: 'Atualizado com sucesso',
              icon: 'success',
              confirmButtonText: 'OK'
            });
            this.carregarRegistros();
            this.limparFormulario();
          },
          error: (error) =>
            Swal.fire({
              title: 'Erro ao Atualizar',
              icon: 'error',
              confirmButtonText: 'OK'
            })
        });
      } else {
        this.clienteService.findById(this.form.value.clienteId).subscribe({
          next:(cliente)=>{
            const dadosParaSalvar = {
              ...this.form.value,
              honorario: this.removerFormatacaoMoeda(this.form.value.honorario),
        despesasAdicionais: this.removerFormatacaoMoeda(this.form.value.despesasAdicionais),
              cliente: cliente
            };
            this.despesaService.save(dadosParaSalvar).subscribe({
              next: () => {
                Swal.fire({
                  title: 'Cadastrado com sucesso',
                  icon: 'success',
                  confirmButtonText: 'OK'
                });
                this.carregarRegistros();
                this.limparFormulario();
              },
              error: (error) =>
                Swal.fire({
                  title: 'Erro ao salvar',
                  icon: 'error',
                  confirmButtonText: 'OK'
                })
            });
          },
          error: (error) => {
            Swal.fire({
              title: 'Erro ao buscar cliente',
              icon: 'error',
              confirmButtonText: 'OK'
            });
          }
        })

      }
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
          error: (error) => {
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

    const numericValue = (parseInt(value) / 100);

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
    (data) => this.clientes = data,
    (error) => console.error('Erro ao buscar clientes', error)
  );
}
}