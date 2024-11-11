import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FinanceiroService } from '../../services/financeiro.service';
import { Financeiro } from '../../services/financeiro.service';
import { CommonModule, registerLocaleData } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import Swal from 'sweetalert2';
import localePt from '@angular/common/locales/pt';

registerLocaleData(localePt, 'pt-BR');

@Component({
  selector: 'app-financeiro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './financeiro.component.html',
  styleUrls: ['./financeiro.component.scss']
})
export class FinanceiroComponent implements OnInit {
  registros: Financeiro[] = [];
  form: FormGroup;
  modoEdicao = false;
  registroSelecionadoId?: number;

  statusOptions = ['PENDENTE', 'PAGO', 'ATRASADO', 'ESTORNADO'];
  formasPagamento = ['PIX', 'CARTÃO', 'DINHEIRO', 'TRANSFERÊNCIA'];
  categoria = ['TRANSPORTE', 'ALIMENTAÇÃO', 'ESCRITÓRIO', 'CAPACITAÇÃO', 'PUBLICIDADE', 'LICENÇAS'];

  constructor(
    private financeiroService: FinanceiroService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      honorado: ['', [Validators.required]],
      formaPagamento: ['', [Validators.required]],
      statusPagamento: ['', [Validators.required]],
      categoria: ['', [Validators.required]],
      dataVencimentoParcelas: ['', [Validators.required]],
      despesasAdicionais: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.carregarRegistros();
  }

  carregarRegistros(): void {
    this.financeiroService.getAllFinanceiro().subscribe({
      next: (data) => {
        this.registros = data; // Atualiza os registros com os dados recebidos
      },
      error: (error) => {
        console.error('Erro ao carregar registros:', error);
      }
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      const formValue = { 
        ...this.form.value,
        honorado: this.removerFormatacaoMoeda(this.form.value.honorado),
        despesasAdicionais: this.removerFormatacaoMoeda(this.form.value.despesasAdicionais)
      };

      if (this.modoEdicao && this.registroSelecionadoId) {
        this.financeiroService.updateFinanceiro(this.registroSelecionadoId, formValue).subscribe({
          next: () => {
            Swal.fire({
              title: 'Atualizado com sucesso',
              icon: 'success',
              confirmButtonText: 'OK'
            });
            this.carregarRegistros(); // Recarrega os registros após a atualização
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
        this.financeiroService.createFinanceiro(formValue).subscribe({
          next: () => {
            Swal.fire({
              title: 'Cadastrado com sucesso',
              icon: 'success',
              confirmButtonText: 'OK'
            });
            this.carregarRegistros(); // Recarrega os registros após a criação
            this.limparFormulario();
          },
          error: (error) =>
            Swal.fire({
              title: 'Erro ao salvar',
              icon: 'error',
              confirmButtonText: 'OK'
            })
        });
      }
    }
  }

  editarRegistro(registro: Financeiro): void {
    this.modoEdicao = true;
    this.registroSelecionadoId = registro.id;
    // Preenche o formulário com os dados do registro selecionado
    this.form.patchValue({
      honorado: registro.honorado,
      formaPagamento: registro.formaPagamento,
      statusPagamento: registro.statusPagamento,
      categoria: registro.categoria,
      dataVencimentoParcelas: registro.dataVencimentoParcelas,
      despesasAdicionais: registro.despesasAdicionais
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
        this.financeiroService.deleteFinanceiro(id).subscribe({
          next: () => {
            Swal.fire({
              title: 'Deletado com sucesso',
              icon: 'success',
              confirmButtonText: 'OK'
            });
            this.carregarRegistros(); // Recarrega os registros após a exclusão
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
}