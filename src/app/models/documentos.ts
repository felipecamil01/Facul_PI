import { Processo } from "./processo";

export class Documentos {

    id!: number;
    listaDocumentos!: string[];  
    dataRecebimento!: Date;  
    statusDocumento!: string; 
    observacao!: string;  
    processos!: Processo[];  
  
  
}
