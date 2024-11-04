import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  http = inject(HttpClient)
  API = "http://localhost:8080/api/cliente"
  constructor() { }
  
  findAll(): Observable<Cliente[]>{
    return this.http.get<Cliente[]>(this.API+"/findAll")
  }

  delete(id:number): Observable<void>{
    return this.http.delete<void>(this.API+"/delete/"+id)
  }

  save(cliente:Cliente): Observable<Cliente>{
    return this.http.post<Cliente>(this.API+"/save", cliente)
  }

  update(id:number , cliente:Cliente): Observable<Cliente>{
    return this.http.put<Cliente>(this.API+"/update/"+ id, cliente)
  }

  findById(id:number): Observable<Cliente>{
    return this.http.get<Cliente>(this.API+"/findById/"+ id)
  }

  findByNome(nome : string): Observable<Cliente>{
    return this.http.get<Cliente>(this.API+"/findByNome/"+ nome)
  }


}
