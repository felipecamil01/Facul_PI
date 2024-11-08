import { Cliente } from "./cliente";

export interface Contato{
    id?:number;    
    dataUltimoContato:string;
    meioContato:string;
    notasContato:string;
    proximoPassos:string;
    clienteId: number | null; 
    cliente?: Cliente; 
}
