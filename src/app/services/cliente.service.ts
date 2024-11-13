import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente.model';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  http = inject(HttpClient)
  API = "http://localhost:8080/api/cliente"
  constructor() { }

  save(cliente:Cliente): Observable<Cliente>{
    return this.http.post<Cliente>(this.API+"/save", cliente)
  }

  update(id:number , cliente:Cliente): Observable<Cliente>{
    return this.http.put<Cliente>(this.API+"/update/"+ id, cliente)
  }

<<<<<<< HEAD
  delete(id:number): Observable<void>{
    return this.http.delete<void>(this.API+"/delete/"+id)
  }

  findAll(): Observable<Cliente[]>{
    return this.http.get<Cliente[]>(this.API+"/findAll")
  }

  findById(id:number): Observable<Cliente>{
    return this.http.get<Cliente>(this.API+"/findById/"+ id)
=======
  findById(id:number): Observable<Cliente[]>{
    return this.http.get<Cliente[]>(this.API+"/findById/"+ id)
>>>>>>> 160ad13fe727af3f0528204f265d2266d6d00e96
  }

  findByNome(nome : string): Observable<Cliente[]>{
    return this.http.get<Cliente[]>(this.API+"/findByNome/"+ nome)
  }

}