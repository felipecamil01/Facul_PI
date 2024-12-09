import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Processo } from '../models/processo.model';
import { environment } from '../../environments/environment'

@Injectable({
  providedIn: 'root'
})
export class ProcessoService {

  http = inject(HttpClient);
  private API = environment.SERVIDOR + "/api/processo"

  constructor() { }

  findAll(): Observable<Processo[]>{
    return this.http.get<Processo[]>(this.API+"/findAll");
  }
  delete(id:number): Observable<void>{
    return this.http.delete<void>(this.API+"/delete/"+id);
  }
  save(processo:Processo): Observable<Processo>{
    return this.http.post<Processo>(this.API+"/save", processo);
  }
  update(id:number , processo:Processo): Observable<Processo>{
    return this.http.put<Processo>(this.API+"/update/"+ id, processo);
  }
  findById(id:number): Observable<Processo>{
    return this.http.get<Processo>(this.API+"/findById/"+ id);
  }
  findByNumero(numero : string): Observable<Processo>{
    return this.http.get<Processo>(this.API+"/findByNumeroProcesso"+ numero);
  }

}
