import { Processo } from './processo.model';

export interface Documento {
  id?: number;
  titulo: string;
  dataRecebimento?: string | Date;
  statusDocumento: string;
  observacao?: string;
  arquivo?: ArrayBuffer | string | null;
  nomeArquivo?: string;
  arquivoPath?: string;
  processo?: Processo | null;
}
