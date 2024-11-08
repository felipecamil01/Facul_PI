import { statusPagamento } from "./status-de-pagamento.enum";

export class financeiro {

    id!:number;
    Honorario!:number;
    Formadepagamento!:string;
    StatusPagamento!:statusPagamento;
    dataDeVencimento!:Date;
    historicoPagamentos!: Date[];
    despesasAdicionais!: number;
}