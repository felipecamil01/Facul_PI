import { Endereco } from "./endereco.model.js";
import { Processo } from "./processo.model.js";
import { StatusClienteEnum } from "./status-cliente.enum.js";
import { Despesa } from "./despesa.model.js";
<<<<<<< HEAD
import { EstadoCivilEnumTs } from "./status-civil.enum";
=======
import { OrgaoExpedidor } from "./EmissorEmissor.enum.js";
>>>>>>> mascara

export class Cliente {
    id!:number;
    nome!:string;
    email!:string;
<<<<<<< HEAD
    cpf!:number;
    rg!:number;
=======
    cpf!:string;
    orgaoEmissor!:OrgaoExpedidor;
    rg!:string;
>>>>>>> mascara
    profissao!:string;
    telefone!:string;
    dataNascimento!:Date;
    estadoCivil!:EstadoCivilEnumTs
    statusCliente!:StatusClienteEnum
    endereco!:Endereco
    processos!:Processo[]
    despesas!:Despesa[]
}