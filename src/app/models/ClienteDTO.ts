import { Endereco } from "./endereco.model";

export interface ClienteDTO {
  id: number;
  nome: string;
  email: string;
  cpf: string; // já vem mascarado do back
  rg: string;
  profissao: string;
  telefone: string;
  dataNascimento: string; // ou Date, dependendo do formato vindo do back
  estadoCivil: 'SOLTEIRO' | 'CASADO' | 'DIVORCIADO' | 'VIUVO'; // ou string simples
  statusCliente: 'ATIVO' | 'INATIVO';
  endereco: Endereco;
}
