import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { ClienteService } from '../../../services/cliente.service';
import { PagamentoService } from '../../../services/pagamentoService';
import { LoginService } from '../../../auth/login.service';
import { Pagamento } from '../../../models/pagamento.model';
import { Cliente } from '../../../models/cliente.model';
import { ClienteDTO } from '../../../models/ClienteDTO';

@Component({
  selector: 'app-pagamento-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './pagamento-form.component.html',
  styleUrls: ['./pagamento-form.component.scss']
})
export class PagamentoFormComponent implements OnInit {
  loginService = inject(LoginService);
  form: FormGroup;
  modoEdicao = false;
  clientes: ClienteDTO[] = [];
  parcelasOptions: number[] = Array.from({length: 15}, (_, i) => i + 1);

  constructor(
    private router: Router,
  private pagamentoService: PagamentoService,
    private fb: FormBuilder,
    private clienteService: ClienteService
  ) {
    this.form = this.fb.group({
      clienteId: [null, Validators.required],
      valorTotal: ['', Validators.required],
      dataPagamento: [null],
      tipoPagamento: ['A_VISTA', Validators.required],
      entrada: [null],
      numeroParcelas: [1],
      observacao: ['']
    });
  }

  ngOnInit(): void {
    this.carregaClientes();
    this.onTipoPagamentoChange();
  }

  carregaClientes(): void {
    this.clienteService.findAll().subscribe(
      (data: ClienteDTO[]) => (this.clientes = data),
      (error: any) => console.error('Erro ao buscar clientes', error)
    );
  }

  onTipoPagamentoChange(): void {
    const tipo = this.form.get('tipoPagamento')?.value;
    const numParcelasControl = this.form.get('numeroParcelas');

    if (tipo === 'PARCELADO') {
      numParcelasControl?.setValidators(Validators.required);
    } else {
      numParcelasControl?.clearValidators();
      this.form.patchValue({ entrada: null, numeroParcelas: 1 });
    }
    numParcelasControl?.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.form.invalid) {
      Swal.fire('Atenção!', 'Por favor, preencha todos os campos obrigatórios.', 'warning');
      return;
    }
    
    this.clienteService.findById(this.form.value.clienteId).subscribe({
      next: (cliente) => {
        const pagamentoData: Pagamento = {
          cliente: cliente,
          valorTotal: this.removerFormatacaoMoeda(this.form.value.valorTotal),
          dataPagamento: this.form.value.dataPagamento ? new Date(this.form.value.dataPagamento).toISOString() : undefined,
          tipoPagamento: this.form.value.tipoPagamento,
          entrada: this.removerFormatacaoMoeda(this.form.value.entrada),
          numeroParcelas: this.form.value.numeroParcelas,
          observacao: this.form.value.observacao,
          formaPagamento: this.form.value.tipoPagamento, // ajuste conforme necessário
          statusPagamento: 'PENDENTE' // ajuste conforme necessário ou obtenha do form
        };

        this.pagamentoService.save(pagamentoData).subscribe({
          next: () => {
            Swal.fire('Sucesso!', 'Pagamento salvo com sucesso.', 'success');
            this.router.navigate([this.getRoute('pagamento')]);
          },
          error: (err: any) => {
            Swal.fire('Erro!', 'Ocorreu um erro ao salvar o pagamento.', 'error');
            console.error(err);
          }
        });
      },
      error: () => Swal.fire('Erro!', 'Não foi possível encontrar o cliente selecionado.', 'error')
    });
  }

    formatCurrency(event: Event): void {
    const input = event.target as HTMLInputElement;
    // Remove tudo que não for número
    let value = input.value.replace(/\D/g, '');
    if (!value) value = '0';
    const numericValue = parseInt(value, 10) / 100;
    const formattedValue = new Intl.NumberFormat('pt-BR', {
      style: 'currency', currency: 'BRL'
    }).format(numericValue);
    input.value = formattedValue;
    // Atualiza o formControl sem disparar eventos extras
    const controlName = input.getAttribute('formControlName');
    if (controlName) {
      this.form.get(controlName)?.setValue(formattedValue, { emitEvent: false });
    }
  }

  private removerFormatacaoMoeda(valor: string | number | null): number {
    if (valor === null || valor === undefined) return 0;
    if (typeof valor === 'number') return valor;
    const valorLimpo = valor.replace(/[R$\s.]/g, '').replace(',', '.');
    const valorNumerico = parseFloat(valorLimpo);
    return isNaN(valorNumerico) ? 0 : valorNumerico;
  }

  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  } 
  
}