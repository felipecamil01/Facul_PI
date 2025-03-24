import { Endereco } from "./endereco.model.js";
import { Processo } from "./processo.model.js";
import { StatusClienteEnum } from "./status-cliente.enum.js";
import { Despesa } from "./despesa.model.js";
import { EstadoCivilEnumTs } from "./status-civil.enum";

export class Cliente {
    id!:number;
    nome!:string;
    email!:string;
    cpf!:string;
    rg!:string;
    profissao!:string;
    telefone!:string;
    dataNascimento!:Date;
    estadoCivil!:EstadoCivilEnumTs
    statusCliente!:StatusClienteEnum
    endereco!:Endereco
    processos!:Processo[]
    despesas!:Despesa[]
}