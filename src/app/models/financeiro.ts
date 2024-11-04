import { StatusClienteEnum } from "./status-cliente.enum";

export class financeiro {

    id!:number;
    Honorario!:number;
    Formadepagamento!:string;
    StatusPagamento!:StatusClienteEnum;
    dataDeVencimento!:Date;
    historicoPagamentos: Date[];
    despesasAdicionais: number;

}