import { Cliente } from "./cliente.model";

export class Agenda{
    id!:number;    
    dataUltimoContato!:string;
    meioContato!:string;
    notasContato!:string;
    proximoPassos!:string;
    cliente!: Cliente; 
}
