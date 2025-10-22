import { Cliente } from './cliente.model';
import { Pagamento } from './pagamento.model';

export interface AgendaResumo {
  id: number;
  descricao: string;
  tipo: string;
  prazoImportante: boolean;
  prioridade?: string | null;
  data: string;
  processoId?: number | null;
  processoNumero?: string | null;
}

export interface DocumentoResumo {
  id: number;
  titulo: string;
  dataRecebimento?: string;
  statusDocumento: string;
  observacao?: string;
  processoNumero?: string;
  processoId?: number;
  caminhoArquivo?: string;
}

export interface ProcessoPrazo {
  data?: string;
  descricao?: string;
}

export interface ProcessoResumo {
  id: number;
  numeroProcesso: string;
  areaAtuacao?: string;
  situacaoAtual?: string;
  dataInicio?: string;
  prazosImportantes?: ProcessoPrazo[];
  documentos: DocumentoResumo[];
}


export interface ClienteOverview {
  cliente: Cliente;
  processos: ProcessoResumo[];
  pagamentos: Pagamento[];
  documentos: DocumentoResumo[];
  prazosImportantes: AgendaResumo[];
}

