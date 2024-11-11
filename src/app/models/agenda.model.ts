import { Cliente } from "./cliente.model";

export interface Contato{
    id?:number;    
    dataUltimoContato:string;
    meioContato:string;
    notasContato:string;
    proximoPassos:string;
    clienteId: number | null; 
    cliente?: Cliente; 
}

