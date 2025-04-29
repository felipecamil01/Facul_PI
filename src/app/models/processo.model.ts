import { Cliente } from "./cliente.model"
import { ClienteDTO } from "./ClienteDTO"
import { Documentos } from "./documentos.model"

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
