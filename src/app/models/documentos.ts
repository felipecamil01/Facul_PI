import { Processo } from "./processo";

export class Documentos {

    id!: number;  // `id` é opcional, pois é gerado pelo backend
    listaDocumentos!: string[];  // Lista de strings que armazena documentos
    dataRecebimento!: Date;  // Data com restrição de passado ou presente
    statusDocumento!: string;  // Status do documento (não pode ser vazio)
    observacao!: string;  // Campo opcional para observações
    processos!: Processo[];  // IDs de processos relacionados (Many-to-Many)
  
  
}
