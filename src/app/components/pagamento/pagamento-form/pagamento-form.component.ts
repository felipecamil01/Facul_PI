import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { ClienteService } from '../../../services/cliente.service';
import { PagamentoService } from '../../../services/pagamentoService';
import { LoginService } from '../../../auth/login.service';
import { Pagamento } from '../../../models/pagamento.model';
import { Observable } from 'rxjs';
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
  parcelasOptions: number[] = Array.from({ length: 15 }, (_, i) => i + 1);
  pagamentoId: number | null = null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private pagamentoService: PagamentoService,
    private fb: FormBuilder,
    private clienteService: ClienteService
  ) {
    this.form = this.fb.group({
      clienteId: [null],
      clienteNome: [''],
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
    const routeIdStr = this.route.snapshot.paramMap.get('id');
    this.pagamentoId = routeIdStr ? parseInt(routeIdStr, 10) : null;
    if (this.pagamentoId) {
      this.modoEdicao = true;
      this.pagamentoService.findById(this.pagamentoId).subscribe({
        next: (p: any) => {
          this.form.patchValue({
            clienteId: p?.cliente?.id,
            clienteNome: p?.cliente?.nome,
            valorTotal: p.valorTotal,
            dataPagamento: p.dataPagamento ? new Date(p.dataPagamento) : null,
            tipoPagamento: p.tipoPagamento,
            entrada: p.entrada,
            numeroParcelas: p.numeroParcelas,
            observacao: p.observacao
          });
          this.onTipoPagamentoChange();
        },
        error: (e) => console.error('Erro ao carregar pagamento', e)
      });
    }
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

    const clienteId: number | null = this.form.value.clienteId;
    const clienteNome: string = (this.form.value.clienteNome || '').trim();

    let carregarCliente$: Observable<any> | null = null;

    if (clienteId) {
      carregarCliente$ = this.clienteService.findById(clienteId);
    } else if (clienteNome) {
      carregarCliente$ = this.clienteService.findByNome(clienteNome);
    }

    if (!carregarCliente$) {
      Swal.fire('Atenção', 'Informe um cliente (digite o nome ou selecione).', 'warning');
      return;
    }

    carregarCliente$.subscribe({
      next: (clienteOuLista: any) => {
        const cliente = Array.isArray(clienteOuLista)
          ? (clienteOuLista.length ? clienteOuLista[0] : null)
          : clienteOuLista;
        if (!cliente) {
          Swal.fire('Atenção', 'Cliente não encontrado. Selecione um existente.', 'warning');
          return;
        }

        const pagamentoData: Pagamento = {
          cliente: cliente,
          valorTotal: this.removerFormatacaoMoeda(this.form.value.valorTotal),
          dataPagamento: this.form.value.dataPagamento ? this.formatDateOnly(this.form.value.dataPagamento) : undefined,
          tipoPagamento: this.form.value.tipoPagamento,
          entrada: this.removerFormatacaoMoeda(this.form.value.entrada),
          numeroParcelas: this.form.value.numeroParcelas,
          observacao: this.form.value.observacao,
          formaPagamento: this.form.value.tipoPagamento,
          statusPagamento: 'PENDENTE'
        };

        const call$ = this.modoEdicao && this.pagamentoId
          ? this.pagamentoService.update(this.pagamentoId, pagamentoData)
          : this.pagamentoService.save(pagamentoData);

        call$.subscribe({
          next: () => {
            Swal.fire('Sucesso!', 'Pagamento salvo com sucesso.', 'success');
            this.router.navigate([this.getRoute('pagamentos')]);
          },
          error: (err: any) => {
            Swal.fire('Erro!', 'Ocorreu um erro ao salvar o pagamento.', 'error');
            console.error(err);
          }
        });
      },
      error: () => Swal.fire('Erro!', 'Não foi possível buscar o cliente.', 'error')
    });
  }

  formatCurrency(event: Event): void {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/\D/g, '');
    if (!value) value = '0';
    const numericValue = parseInt(value, 10) / 100;
    const formattedValue = new Intl.NumberFormat('pt-BR', {
      style: 'currency', currency: 'BRL'
    }).format(numericValue);
    input.value = formattedValue;
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

  private formatDateOnly(value: any): string {
    const d = value instanceof Date ? value : new Date(value);
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }
}
