import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FinanceiroService } from '../../services/financeiro.service';
import { Financeiro } from '../../services/financeiro.service';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import Swal from 'sweetalert2';
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
  
  statusOptions = ['PENDENTE', 'PAGO', 'ATRASADO', 'CANCELADO'];
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
            Swal.fire({
              title:'Atualizado com sucesso',
              icon:'success',
              confirmButtonText:'OK'
            })
            this.carregarRegistros();
            this.limparFormulario();
          },
          error: (error) => 
            Swal.fire({
              title:'Erro ao Atualizar',
              icon: 'error',
              confirmButtonText:'OK'
            })
        });
      } else {
        this.financeiroService.createFinanceiro(this.form.value).subscribe({
          next: () => {
            Swal.fire({
              title:'Cadastrado com sucesso',
              icon:'success',
              confirmButtonText:'OK'
            })
            this.carregarRegistros();
            this.limparFormulario();
          },
          error: (error) =>
            Swal.fire({
              title:'Erro ao salvar',
              icon: 'error',
              confirmButtonText:'OK'
            })
        });
      }
    }
  }

  editarRegistro(registro: Financeiro): void {
    this.modoEdicao = true;
    this.registroSelecionadoId = registro.id;
    this.form.patchValue(registro);
  }

  excluirRegistro(id:number):void{
    Swal.fire({
      title:'Quer deletar este registro?',
      icon:'warning',
      showConfirmButton:true,
      showDenyButton:true,
      confirmButtonText:'Sim',
      denyButtonText:'Não',
    }).then((result)=>{
      if (result.isConfirmed) {
        this.financeiroService.deleteFinanceiro(id).subscribe({
          next: lista=>{
            Swal.fire({
              title:'Deletado com sucesso',
              icon:'success',
              confirmButtonText:'ok'
            })
            this.carregarRegistros();
          },
          error: erro=>{
            Swal.fire({
              title:'Erro ao deletar',
              icon: 'error',
              confirmButtonText:'OK'
            })
          }
        })
      }
    }
    )
 }

  limparFormulario(): void {
    this.form.reset();
    this.modoEdicao = false;
    this.registroSelecionadoId = undefined;
  }
}
