import { statusPagamento } from "./status-pagamento.enum";
import { Cliente } from './cliente.model';

export class Financeiro {

    id!:number;
    Honorario!:number;
    Formadepagamento!:string;
    StatusPagamento!:statusPagamento;
    dataDeVencimento!:Date;
    historicoPagamentos!: Date[];
    despesasAdicionais!: number;
    clienteId!: number;
    cliente!: Cliente;
}