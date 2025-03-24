import { Agenda } from "./agenda.model"
import { Cliente } from "./cliente.model"
import { Documento } from "./documento.model"

export class Processo {
    id!:number
    tipoCliente!:string
    areaAtuacao!:string
    numeroProcesso!:string
    comarca!:string
    dataInicio!:Date
    descricao!:string
    andamento!:string
    situacaoAtual!:string
    cliente!:Cliente
    agendas!:Agenda[];
    documentos!:Documento[]
}