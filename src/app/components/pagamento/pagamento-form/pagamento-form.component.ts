import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { debounceTime, takeUntil } from 'rxjs/operators';
import { Observable, Subject } from 'rxjs';
import Swal from 'sweetalert2';
import { LoginService } from '../../../auth/login.service';
import { ClienteDTO } from '../../../models/ClienteDTO';
import { Pagamento } from '../../../models/pagamento.model';
import { ClienteService } from '../../../services/cliente.service';
import { PagamentoService } from '../../../services/pagamentoService';

interface ParcelaPreview {
  numero: number;
  valor: number;
  vencimento: Date;
}

@Component({
  selector: 'app-pagamento-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './pagamento-form.component.html',
  styleUrls: ['./pagamento-form.component.scss']
})
export class PagamentoFormComponent implements OnInit, OnDestroy {
  loginService = inject(LoginService);
  form: FormGroup;
  modoEdicao = false;
  clientes: ClienteDTO[] = [];
  parcelasOptions: number[] = Array.from({ length: 15 }, (_, i) => i + 1);
  pagamentoId: number | null = null;

  valorAParcelarPreview = 0;
  valorParcelasSomaPreview = 0;
  valorParcelaPreview = 0;
  parcelasPreview: ParcelaPreview[] = [];

  private destroy$ = new Subject<void>();

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
      numeroParcelas: [1, Validators.min(1)],
      observacao: ['']
    });
  }

  ngOnInit(): void {
    this.carregaClientes();
    this.onTipoPagamentoChange();
    this.monitorarMudancas();

    const routeIdStr = this.route.snapshot.paramMap.get('id');
    this.pagamentoId = routeIdStr ? parseInt(routeIdStr, 10) : null;
    if (this.pagamentoId) {
      this.modoEdicao = true;
      this.pagamentoService.findById(this.pagamentoId).subscribe({
        next: (p: Pagamento) => {
          this.form.patchValue({
            clienteId: p?.cliente?.id,
            clienteNome: p?.cliente?.nome,
            valorTotal: this.formatarMoeda(p.valorTotal),
            dataPagamento: p.dataPagamento ? new Date(p.dataPagamento) : null,
            tipoPagamento: p.tipoPagamento,
            entrada: p.entrada != null ? this.formatarMoeda(p.entrada) : null,
            numeroParcelas: p.numeroParcelas || 1,
            observacao: p.observacao || ''
          });
          this.onTipoPagamentoChange();
          this.recalcularResumoParcelas();
        },
        error: (e) => console.error('Erro ao carregar pagamento', e)
      });
    } else {
      this.recalcularResumoParcelas();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
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
      numParcelasControl?.setValidators([Validators.required, Validators.min(1)]);
    } else {
      numParcelasControl?.clearValidators();
      this.form.patchValue({ entrada: null, numeroParcelas: 1 }, { emitEvent: false });
    }
    numParcelasControl?.updateValueAndValidity({ emitEvent: false });
    this.recalcularResumoParcelas();
  }

  onSubmit(): void {
    if (this.form.invalid) {
      Swal.fire('Atenção!', 'Por favor, corrija os campos destacados.', 'warning');
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
          numeroParcelas: this.form.value.tipoPagamento === 'PARCELADO' ? Number(this.form.value.numeroParcelas) : 1,
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
    const formattedValue = this.formatarMoeda(numericValue);
    input.value = formattedValue;
    const controlName = input.getAttribute('formControlName');
    if (controlName) {
      this.form.get(controlName)?.setValue(formattedValue, { emitEvent: false });
    }
    this.recalcularResumoParcelas();
  }

  private formatarMoeda(valor: number | null | undefined): string {
    const numero = valor ?? 0;
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(numero);
  }

  private removerFormatacaoMoeda(valor: string | number | null): number {
    if (valor === null || valor === undefined) return 0;
    if (typeof valor === 'number') return valor;
    const valorLimpo = valor.replace(/[R$\s.]/g, '').replace(',', '.');
    const valorNumerico = parseFloat(valorLimpo);
    return isNaN(valorNumerico) ? 0 : this.round2(valorNumerico);
  }

  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  }

  private monitorarMudancas(): void {
    this.form.valueChanges
      .pipe(debounceTime(150), takeUntil(this.destroy$))
      .subscribe(() => this.recalcularResumoParcelas());
  }

  private recalcularResumoParcelas(): void {
    const tipo = this.form.get('tipoPagamento')?.value;
    const valorTotal = this.removerFormatacaoMoeda(this.form.value.valorTotal);
    const entrada = this.removerFormatacaoMoeda(this.form.value.entrada);
    const numeroParcelas = Number(this.form.value.numeroParcelas || 1);

    this.toggleControlError('entrada', 'maiorQueTotal', entrada > valorTotal);

    const totalAParcelar = Math.max(valorTotal - entrada, 0);
    this.valorAParcelarPreview = this.round2(totalAParcelar);

    if (tipo === 'PARCELADO' && numeroParcelas > 0) {
      const valorBase = this.round2(totalAParcelar / numeroParcelas);
      const totalArredondado = this.round2(valorBase * numeroParcelas);
      const diferenca = this.round2(totalAParcelar - totalArredondado);

      const dataBase = this.form.value.dataPagamento ? new Date(this.form.value.dataPagamento) : new Date();
      const preview: ParcelaPreview[] = [];
      let soma = 0;

      for (let i = 1; i <= numeroParcelas; i++) {
        const vencimento = new Date(dataBase);
        vencimento.setMonth(vencimento.getMonth() + (i - 1));

        let valor = valorBase;
        if (i === numeroParcelas) {
          valor = this.round2(valorBase + diferenca);
        }

        soma = this.round2(soma + valor);
        preview.push({ numero: i, valor, vencimento });
      }

      const ajuste = this.round2(totalAParcelar - soma);
      if (Math.abs(ajuste) >= 0.01 && preview.length) {
        const ultima = preview[preview.length - 1];
        ultima.valor = this.round2(ultima.valor + ajuste);
        soma = this.round2(preview.reduce((acc, item) => acc + item.valor, 0));
      }

      this.parcelasPreview = preview;
      this.valorParcelaPreview = preview.length ? preview[0].valor : 0;
      this.valorParcelasSomaPreview = soma;
    } else {
      const dataBase = this.form.value.dataPagamento ? new Date(this.form.value.dataPagamento) : new Date();
      this.parcelasPreview = [{
        numero: 1,
        valor: this.round2(totalAParcelar),
        vencimento: dataBase
      }];
      this.valorParcelaPreview = this.round2(totalAParcelar);
      this.valorParcelasSomaPreview = this.round2(totalAParcelar);
    }
  }

  private toggleControlError(controlName: string, errorKey: string, ativar: boolean): void {
    const control = this.form.get(controlName);
    if (!control) return;
    const errors = { ...(control.errors || {}) };
    if (ativar) {
      errors[errorKey] = true;
      control.setErrors(errors);
    } else if (errors[errorKey]) {
      delete errors[errorKey];
      const empty = Object.keys(errors).length === 0;
      control.setErrors(empty ? null : errors);
    }
  }

  private round2(valor: number): number {
    return Math.round((valor + Number.EPSILON) * 100) / 100;
  }

  formatDateOnly(value: any): string {
    const d = value instanceof Date ? value : new Date(value);
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  get valorTotalCalculado(): number {
    return this.removerFormatacaoMoeda(this.form.value.valorTotal);
  }

  get entradaCalculada(): number {
    return this.removerFormatacaoMoeda(this.form.value.entrada);
  }
}
