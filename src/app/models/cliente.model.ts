import { Endereco } from "./endereco.model.js";
import { Processo } from "./processo.model.js";
import { StatusClienteEnum } from "./status-cliente.enum.js";

import { OrgaoExpedidor } from "./EmissorEmissor.enum.js";
import { EstadoCivilEnumTs } from "./status-civil.enum.js";
import { Pagamento } from "./pagamento.model.js";

export class Cliente {
    id!:number;
    nome!:string;
    email!:string;
    cpf!:string;
    orgaoEmissor!:OrgaoExpedidor;
    rg!:string;
    profissao!:string;
    telefone!:string;
    dataNascimento!:Date;
    estadoCivil!:EstadoCivilEnumTs
    statusCliente!:StatusClienteEnum
    endereco!:Endereco
    processos!:Processo[]
    pagamentos!:Pagamento[];
}