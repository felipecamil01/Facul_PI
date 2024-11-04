import { Cliente } from "./cliente"
import { Documentos } from "./documentos"

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
    prazosImportantes!:Date[]
    cliente!:Cliente
    documentos!:Documentos[]
}
