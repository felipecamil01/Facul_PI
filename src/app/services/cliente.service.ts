import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente.model';
import { environment } from '../../environments/environment'
import { ClienteDTO } from '../models/ClienteDTO';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  http = inject(HttpClient)
  private API = environment.SERVIDOR + "/api/cliente"
  constructor() { }

  save(cliente:Cliente): Observable<Cliente>{
    return this.http.post<Cliente>(this.API+"/save", cliente)
  }

  update(id:number , cliente:Cliente): Observable<Cliente>{
    return this.http.put<Cliente>(this.API+"/update/"+ id, cliente)
  }

  delete(id:number): Observable<void>{
    return this.http.delete<void>(this.API+"/delete/"+id)
  }

  findAll(): Observable<ClienteDTO[]>{
    return this.http.get<ClienteDTO[]>(this.API+"/findAll")
  }

  findById(id:number): Observable<Cliente>{
    return this.http.get<Cliente>(this.API+"/findById/"+ id)
  }

  findByNome(nome : string): Observable<ClienteDTO[]>{
    return this.http.get<ClienteDTO[]>(this.API+"/findByNome/"+ nome)
  }

}