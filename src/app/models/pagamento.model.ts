import { Cliente } from './cliente.model';
import { Parcela } from './parcela.model';
import { StatusPagamento } from './status-pagamento.type';
import { TipoPagamento } from './tipo-pagamento.type';

export interface Pagamento {
  id?: number;
  cliente: Cliente;
  valorTotal: number;
  tipoPagamento: TipoPagamento;
  entrada?: number | null;
  numeroParcelas?: number | null;
  observacao?: string | null;
  dataCriacao?: string;
  dataPagamento?: string;
  parcelas?: Parcela[];
  formaPagamento?: TipoPagamento | string;
  statusPagamento?: StatusPagamento;
  valorAParcelar?: number;
  valorParcelaCalculada?: number;
}
