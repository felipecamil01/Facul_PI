import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente.model';
import { environment } from '../../environments/environment'

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  http = inject(HttpClient)
  private API = environment.SERVIDOR + "/cliente"
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

  findAll(): Observable<Cliente[]>{
    return this.http.get<Cliente[]>(this.API+"/findAll")
  }

  findById(id:number): Observable<Cliente>{
    return this.http.get<Cliente>(this.API+"/findById/"+ id)
  }

  findByNome(nome : string): Observable<Cliente[]>{
    return this.http.get<Cliente[]>(this.API+"/findByNome/"+ nome)
  }

}