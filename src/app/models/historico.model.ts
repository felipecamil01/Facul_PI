// Interface para o DTO geral que você usa na lista principal
export interface HistoricoGeral {
  entidade: string;
  entidadeId: number;
  revisao: number;
  usuario: string;
  data: string; // O tipo 'Instant' do Java se torna uma string no JSON
  dados: any;   // O tipo 'Object' do Java pode ser 'any' ou um objeto mais específico
}

// Interface para o DTO de um histórico específico (opcional por agora, mas bom ter)
export interface Historico {
  revisao: number;
  usuario: string;
  data: string;
  dados: any;
}