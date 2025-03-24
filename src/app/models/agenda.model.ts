import { Processo } from "./processo.model";

export class Agenda{
    id!:number;    
    data!:Date;
    tipo!:string;
    descricao!:string;
    processo!:Processo;
}