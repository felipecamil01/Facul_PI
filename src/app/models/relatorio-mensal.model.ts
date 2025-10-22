import { StatusPagamento } from './status-pagamento.type';
import { TipoPagamento } from './tipo-pagamento.type';

export interface RelatorioMensalItem {
  pagamentoId: number;
  parcelaId: number;
  clienteNome: string;
  numeroParcela: number;
  dataVencimento: string;
  statusPagamento: StatusPagamento;
  valorParcela: number;
  tipoPagamento: TipoPagamento;
}

export interface RelatorioMensal {
  ano: number;
  mes: number;
  clienteId?: number;
  clienteNome?: string;
  filtroStatus?: StatusPagamento;
  totalGeral: number;
  totalPago: number;
  totalAtrasado: number;
  totalPendente: number;
  itens: RelatorioMensalItem[];
}
