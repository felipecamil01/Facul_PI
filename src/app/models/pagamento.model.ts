import { Cliente } from './cliente.model';
import { Parcela } from './parcela.model';

export interface Pagamento {
  formaPagamento: any;
  statusPagamento: unknown;
  id?: number;
  cliente: Cliente;
  valorTotal: number;
  tipoPagamento: 'A_VISTA' | 'PARCELADO';
  entrada?: number;
  numeroParcelas?: number;
  observacao?: string;
  dataCriacao?: string; // O backend enviará como string no formato ISO
  dataPagamento?: string; // data em que o pagamento foi efetuado (opcional)
  parcelas?: Parcela[];
  // campos computados vindos do backend
  valorAParcelar?: number;
  valorParcelaCalculada?: number;
}
