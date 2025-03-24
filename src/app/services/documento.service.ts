import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment'
import { Documento } from '../models/documento.model'

@Injectable({
  providedIn: 'root'
})
export class DocumentoService {
  http = inject(HttpClient)
  private API = environment.SERVIDOR + "/api/documento"

  constructor() {}

  save(documento: Documento): Observable<Documento> {
    return this.http.post<Documento>(`${this.API}/save`, documento);
  }

  update(id: number, documento: Documento): Observable<Documento> {
    return this.http.put<Documento>(`${this.API}/update/${id}`, documento);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/delete/${id}`);
  }

  findAll(): Observable<Documento[]> {
    return this.http.get<Documento[]>(`${this.API}/findAll`);
  }

  findById(id: number): Observable<Documento> {
    return this.http.get<Documento>(`${this.API}/findById/${id}`);
  }

  findStatusDocumento(): Observable<string[]> {
    return this.http.get<string[]>(this.API+"/findStatusDocumento");
  }
}
