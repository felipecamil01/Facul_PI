import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Agenda } from "../models/agenda.model";
import { environment } from '../../environments/environment'

@Injectable({
providedIn:'root'
})

export class AgendaService {
  private apiUrl = environment.SERVIDOR + "/api/contato";
  
  constructor(private http:HttpClient){}
  
  save(agenda: Agenda): Observable<Agenda> {
    return this.http.post<Agenda>(this.apiUrl+"/save", agenda);
  }

  update(id: number, agenda:Agenda): Observable<Agenda> {
    return this.http.put<Agenda>(this.apiUrl+"/update/"+ id, agenda)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl+"/delete"}/${id}`);
  }

  findAll():Observable<Agenda[]>{
    return this.http.get<Agenda[]>(this.apiUrl+"/findAll");
  }

  findById(id: number): Observable<Agenda> {
    return this.http.get<Agenda>(`${this.apiUrl}/findById/${id}`);
  }
  
  findByIdCliente(clienteId: number): Observable<Agenda[]> {
    return this.http.get<Agenda[]>(`${this.apiUrl}/cliente/${clienteId}`);
  }
    
}