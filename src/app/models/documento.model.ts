import { Processo } from "./processo.model";

export class Documento {
    id!: number;
    descricao!:string;
    dataRecebimento!: Date;
    statusDocumento!: string;
    observacao!: string;
    arquivo!: ArrayBuffer;
    processo!: Processo;
}