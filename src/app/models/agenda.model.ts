export interface Agenda {
  id?: number;         // O '?' torna o ID opcional
  titulo: string;
  descricao?: string;
  tipo: string;
  data: string;
  processo?: any;      // O '?' torna o processo opcional
}