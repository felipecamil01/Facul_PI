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
  parcelas?: Parcela[];
}