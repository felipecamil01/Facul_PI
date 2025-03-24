import { StatusPagamento } from "./status-pagamento.enum";
import { Cliente } from './cliente.model';

export class Despesa {
    id!:number;
    honorario!:number;
    despesasAdicionais!:number;
    dataVencimento!:Date;
    categoriaDespesa!:string;
    formaPagamento!:string;
    statusPagamento!:StatusPagamento;
    observacao!:String;
    cliente!: Cliente;
}