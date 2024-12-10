import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Despesa } from '../models/despesa.model';
import { StatusPagamento } from '../models/status-pagamento.enum';
import { environment } from '../../environments/environment'

@Injectable({
  providedIn: 'root'
})
export class DespesaService {
  http = inject(HttpClient)
  private API = environment.SERVIDOR + "/api/despesa"

  constructor() {}

  save(despesa: Despesa): Observable<Despesa> {
    return this.http.post<Despesa>(`${this.API}/save`, despesa);
  }

  update(id: number, despesa: Despesa): Observable<Despesa> {
    return this.http.put<Despesa>(`${this.API}/update/${id}`, despesa);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/delete/${id}`);
  }

  findAll(): Observable<Despesa[]> {
    return this.http.get<Despesa[]>(`${this.API}/findAll`);
  }

  findById(id: number): Observable<Despesa> {
    return this.http.get<Despesa>(`${this.API}/findById/${id}`);
  }

  findByStatus(statusPagamento: StatusPagamento): Observable<Despesa[]> {
    return this.http.get<Despesa[]>(`${this.API}/findByStatus/${statusPagamento}`);
  }

  findByPagamentoPendente(): Observable<Despesa[]> {
    return this.http.get<Despesa[]>(`${this.API}/findByPagamentoPendente`);
  }

  findByVencimento(statusPagamento: StatusPagamento, data: string): Observable<Despesa[]> {
    return this.http.get<Despesa[]>(`${this.API}/findByVencimento/${statusPagamento}/${data}`);
  }
  getCategoriasMaisUsadas(): Observable<string[]> {
    return this.http.get<string[]>(this.API+"/categorias");
  }
}
