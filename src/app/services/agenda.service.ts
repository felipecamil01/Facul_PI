import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Contato } from "../models/agenda.model";
@Injectable({
providedIn:'root'
})

export class AgendaService {
  private apiUrl = 'http://localhost:8080/api/contato';
  
  constructor(private http:HttpClient){}
  
  registrarContato(contato: Contato): Observable<Contato> {
    return this.http.post<Contato>(this.apiUrl+"/save", contato);
  }

  updateContato(contato: Contato): Observable<Contato> {
    return this.http.put<Contato>(`${this.apiUrl+"/update"}/${contato.id}`, contato);
  }

  deleteContato(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl+"/delete"}/${id}`);
  }

  getContatos():Observable<Contato[]>{
    return this.http.get<Contato[]>(this.apiUrl+"/findAll");
  }

  getContatosById(id: number): Observable<Contato> {
    return this.http.get<Contato>(`${this.apiUrl}/findById/${id}`);
  }
  
  listarContatosPorCliente(clienteId: number): Observable<Contato[]> {
    return this.http.get<Contato[]>(`${this.apiUrl}/cliente/${clienteId}`);
  }
    
}
