import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FinanceiroService } from '../../services/financeiro.service';
import { Financeiro } from '../../services/financeiro.service';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
@Component({
  selector: 'app-financeiro',
  standalone:true,
  imports:[CommonModule,ReactiveFormsModule,HttpClientModule],
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

  constructor(
    private financeiroService: FinanceiroService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      honorado: ['', [Validators.required]],
      formaPagamento: ['', [Validators.required]],
      statusPagamento: ['', [Validators.required]],
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
        this.financeiroService.updateFinanceiro(this.registroSelecionadoId, this.form.value).subscribe({
          next: () => {
            this.carregarRegistros();
            this.limparFormulario();
          },
          error: (error) => console.error('Erro ao atualizar:', error)
        });
      } else {
        this.financeiroService.createFinanceiro(this.form.value).subscribe({
          next: () => {
            this.carregarRegistros();
            this.limparFormulario();
          },
          error: (error) => console.error('Erro ao criar:', error)
        });
      }
    }
  }

  editarRegistro(registro: Financeiro): void {
    this.modoEdicao = true;
    this.registroSelecionadoId = registro.id;
    this.form.patchValue(registro);
  }

  excluirRegistro(id: number): void {
    if (confirm('Deseja realmente excluir este registro?')) {
      this.financeiroService.deleteFinanceiro(id).subscribe({
        next: () => {
          this.carregarRegistros();
        },
        error: (error) => console.error('Erro ao excluir:', error)
      });
    }
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
}
