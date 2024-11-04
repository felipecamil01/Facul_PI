import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { map } from "rxjs";
import { Contato } from "../models/agenda.model";
@Injectable({
providedIn:'root'
})

export class AgendaService {
    private apiUrl='http://localhost:8080/api/contato';
       private API ='http://localhost:8080/api/cliente';
    //Url da API
    constructor(private http:HttpClient){}

    
    getContatos():Observable<Contato[]>{
        return this.http.get<Contato[]>(this.apiUrl+"/findAll");

    }
   
    updateContato(contato: Contato): Observable<Contato> {
        return this.http.put<Contato>(`${this.apiUrl+"/update"}/${contato.id}`, contato);
      }

      deleteContato(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl+"/delete"}/${id}`);
      }
      registrarContato(contato: Contato): Observable<Contato> {
        console.log('Salvando contato:', contato); // Debug
        return this.http.post<Contato>(this.apiUrl+"/save", contato);
      }
    
      listarContatosPorCliente(clienteId: number): Observable<Contato[]> {
        return this.http.get<Contato[]>(`${this.apiUrl}/cliente/${clienteId}`);
      }
    
}
