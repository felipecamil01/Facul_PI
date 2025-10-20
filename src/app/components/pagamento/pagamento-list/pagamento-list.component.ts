import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { Pagamento } from '../../../models/pagamento.model';
import { PagamentoService } from '../../../services/pagamentoService';
import { Parcela } from '../../../models/parcela.model';
import { LoginService } from '../../../auth/login.service';

@Component({
  selector: 'app-pagamento-list',
  standalone: true,
  imports: [CommonModule, RouterLink, CurrencyPipe, DatePipe, FormsModule],
  templateUrl: './pagamento-list.component.html',
  styleUrls: ['./pagamento-list.component.scss']
})
export class PagamentoListComponent implements OnInit {
  loginService = inject(LoginService);
  lista: Pagamento[] = [];
  pagamentoSelecionado?: Pagamento;
  modalDetalhesAberto = false;
  parcelasEmProcessamento = new Set<string>();
  // filtros
  filtroData?: string | null = null; // yyyy-mm-dd
  filtroMes?: string | null = null; // yyyy-mm
  filtroAno?: number | null = null;
  pagamentoService = inject(PagamentoService);

  ngOnInit(): void {
    this.findAll();
  }

  aplicarFiltros(): void {
    // aplica filtros simples no frontend
    this.pagamentoService.findAll().subscribe({
      next: (lista: Pagamento[]) => {
        let res = lista.map((pagamento) => this.normalizarPagamento(pagamento));
        if (this.filtroData) {
          res = res.filter(p => p.dataPagamento && p.dataPagamento.startsWith(this.filtroData!));
        }
        if (this.filtroMes) {
          res = res.filter(p => p.dataPagamento && p.dataPagamento.startsWith(this.filtroMes!));
        }
        if (this.filtroAno) {
          res = res.filter(p => p.dataPagamento && new Date(p.dataPagamento).getFullYear() === this.filtroAno);
        }
        this.lista = res;
      },
      error: (erro: any) => console.error(erro)
    });
  }

  limparFiltros(): void {
    this.filtroData = null;
    this.filtroMes = null;
    this.filtroAno = null;
    this.findAll();
  }

  gerarRelatorioMensal(): void {
    const year = this.filtroAno || new Date().getFullYear();
    const month = this.filtroMes ? parseInt(this.filtroMes.split('-')[1], 10) : new Date().getMonth() + 1;
    this.pagamentoService.getRelatorioMensal(year, month).subscribe({
      next: (data) =>
        this.lista = data.map((pagamento) => this.normalizarPagamento(pagamento)),
      error: (err) => console.error(err)
    });
  }

  gerarRelatorioAnual(): void {
    const year = this.filtroAno || new Date().getFullYear();
    this.pagamentoService.getRelatorioAnual(year).subscribe({
      next: (data) =>
        this.lista = data.map((pagamento) => this.normalizarPagamento(pagamento)),
      error: (err) => console.error(err)
    });
  }

  findAll() {
    this.pagamentoService.findAll().subscribe({
      next: (lista: Pagamento[]) => {
        this.lista = lista.map((pagamento) => this.normalizarPagamento(pagamento));
      },
      error: (erro: any) => {
        console.error('Ocorreu um erro:', erro);
        Swal.fire('Erro!', 'Não foi possível carregar a lista de pagamentos.', 'error');
      },
    });
  }

  normalizarPagamento(pagamento: Pagamento): Pagamento {
    if (!pagamento.parcelas || pagamento.parcelas.length === 0) {
      return { ...pagamento };
    }

    const inicioHoje = new Date();
    inicioHoje.setHours(0, 0, 0, 0);

    const parcelasOrdenadas = pagamento.parcelas
      .map((parcela) => {
        const novaParcela: Parcela = { ...parcela };
        const dataVencimento = new Date(parcela.dataVencimento);
        dataVencimento.setHours(0, 0, 0, 0);

        if (novaParcela.statusPagamento !== 'PAGO') {
          novaParcela.statusPagamento = dataVencimento < inicioHoje ? 'ATRASADO' : 'PENDENTE';
        }

        return novaParcela;
      })
      .sort(
        (a, b) =>
          new Date(a.dataVencimento).getTime() -
          new Date(b.dataVencimento).getTime()
      );

    return { ...pagamento, parcelas: parcelasOrdenadas };
  }

  abrirDetalhes(pagamento: Pagamento): void {
    this.pagamentoSelecionado = this.normalizarPagamento(pagamento);
    this.modalDetalhesAberto = true;
  }

  fecharDetalhes(): void {
    this.modalDetalhesAberto = false;
    this.pagamentoSelecionado = undefined;
  }

  atualizarStatusParcela(
    pagamento: Pagamento,
    parcela: Parcela,
    status: 'PAGO' | 'PENDENTE'
  ): void {
    if (!pagamento.id) {
      return;
    }

    const chave = `${pagamento.id}-${parcela.numeroParcela}`;
    this.parcelasEmProcessamento.add(chave);

    const payload: Pagamento = {
      ...pagamento,
      parcelas: pagamento.parcelas?.map((parcelaAtual) =>
        parcelaAtual.numeroParcela === parcela.numeroParcela
          ? { ...parcelaAtual, statusPagamento: status }
          : { ...parcelaAtual }
      ),
    };

    this.pagamentoService
      .update(pagamento.id, payload)
      .pipe(finalize(() => this.parcelasEmProcessamento.delete(chave)))
      .subscribe({
        next: (pagamentoAtualizado) => {
          Swal.fire('Sucesso!', 'Parcela atualizada com sucesso.', 'success');
          this.findAll();
          if (
            this.pagamentoSelecionado &&
            this.pagamentoSelecionado.id === pagamentoAtualizado.id
          ) {
            this.pagamentoSelecionado = this.normalizarPagamento(
              pagamentoAtualizado
            );
          }
        },
        error: (erro: any) => {
          console.error(erro);
          Swal.fire(
            'Erro!',
            'Não foi possível atualizar o status da parcela.',
            'error'
          );
        },
      });
  }

  isAtualizandoParcela(pagamento: Pagamento, parcela: Parcela): boolean {
    if (!pagamento.id) {
      return false;
    }
    return this.parcelasEmProcessamento.has(
      `${pagamento.id}-${parcela.numeroParcela}`
    );
  }

  getValorPago(pagamento: Pagamento): number {
    if (pagamento.tipoPagamento === 'A_VISTA') {
      return pagamento.dataPagamento ? pagamento.valorTotal : 0;
    }

    const entrada = pagamento.entrada ?? 0;
    const valorParcelasPagas = (pagamento.parcelas || [])
      .filter((parcela) => parcela.statusPagamento === 'PAGO')
      .reduce((total, parcela) => total + parcela.valorParcela, 0);

    return entrada + valorParcelasPagas;
  }

  getSaldoDevedor(pagamento: Pagamento): number {
    const saldo = pagamento.valorTotal - this.getValorPago(pagamento);
    return saldo > 0 ? saldo : 0;
  }

  getParcelasPagas(pagamento: Pagamento): number {
    return (pagamento.parcelas || []).filter(
      (parcela) => parcela.statusPagamento === 'PAGO'
    ).length;
  }

  getParcelasTotais(pagamento: Pagamento): number {
    return pagamento.parcelas?.length || (pagamento.numeroParcelas ?? 0);
  }

  getParcelasRestantes(pagamento: Pagamento): number {
    const totais = this.getParcelasTotais(pagamento);
    const pagas = this.getParcelasPagas(pagamento);
    return totais - pagas > 0 ? totais - pagas : 0;
  }

  getStatusPagamento(pagamento: Pagamento): string {
    if (this.getSaldoDevedor(pagamento) === 0) {
      return 'Concluído';
    }

    if (this.temParcelaAtrasada(pagamento)) {
      return 'Atrasado';
    }

    return 'Em dia';
  }

  getStatusBadge(pagamento: Pagamento): string {
    const status = this.getStatusPagamento(pagamento);

    switch (status) {
      case 'Concluído':
        return 'bg-success';
      case 'Atrasado':
        return 'bg-danger';
      default:
        return 'bg-warning text-dark';
    }
  }

  temParcelaAtrasada(pagamento: Pagamento): boolean {
    return (pagamento.parcelas || []).some(
      (parcela) => parcela.statusPagamento === 'ATRASADO'
    );
  }

  getProximaParcela(pagamento: Pagamento): Parcela | undefined {
    return (pagamento.parcelas || []).find(
      (parcela) => parcela.statusPagamento !== 'PAGO'
    );
  }

  delete(pagamento: Pagamento) {
    Swal.fire({
      title: 'Tem certeza?',
      text: `Deseja deletar o registro de pagamento para ${pagamento.cliente.nome}? Esta ação não pode ser desfeita.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sim, deletar!',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.pagamentoService.delete(pagamento.id!).subscribe({
          next: () => {
            Swal.fire('Deletado!', 'O registro foi removido com sucesso.', 'success');
            this.findAll(); // Recarrega a lista
          },
          error: (err: any) => {
            Swal.fire('Erro!', 'Não foi possível deletar o registro.', 'error');
            console.error(err);
          }
        });
      }
    });
  }

  getProgresso(pagamento: Pagamento): string {
    const total = this.getParcelasTotais(pagamento);
    if (!total) {
      return '—';
    }

    const pagas = this.getParcelasPagas(pagamento);
    return `${pagas} / ${total}`;
  }

  getProgressoBadge(pagamento: Pagamento): string {
    const total = this.getParcelasTotais(pagamento);
    const pagas = this.getParcelasPagas(pagamento);

    if (!total) {
      return 'bg-secondary';
    }

    if (pagas === total) {
      return 'bg-success';
    }

    if (this.temParcelaAtrasada(pagamento)) {
      return 'bg-danger';
    }

    if (pagas > 0) {
      return 'bg-primary';
    }

    return 'bg-warning text-dark';
  }

  getClasseStatusParcela(status: Parcela['statusPagamento']): string {
    switch (status) {
      case 'PAGO':
        return 'badge bg-success';
      case 'ATRASADO':
        return 'badge bg-danger';
      default:
        return 'badge bg-warning text-dark';
    }
  }

  traduzirStatusParcela(status: Parcela['statusPagamento']): string {
    switch (status) {
      case 'PAGO':
        return 'Pago';
      case 'ATRASADO':
        return 'Atrasado';
      default:
        return 'Pendente';
    }
  }

  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  }
}