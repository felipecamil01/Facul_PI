import { Endereco } from "./endereco.js";
import { EstadoCivilEnumTs } from "./estado-civil.enum.ts";
import { Processo } from "./processo.js";
import { StatusClienteEnum } from "./status-cliente.enum.js";


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

}
