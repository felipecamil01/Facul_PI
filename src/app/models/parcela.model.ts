export interface Parcela {
  id?: number;
  numeroParcela: number;
  valorParcela: number;
  dataVencimento: string;
  statusPagamento: 'PENDENTE' | 'PAGO' | 'ATRASADO';
}