export interface Agenda {
  id?: number;           // opcional
  titulo: string;
  descricao?: string;
  tipo: string;
  data: string;
  hora?: string;         // pode adicionar este campo se existir
  processo?: any;        // opcional
}
