import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Financeiro {
  id?: number;
  honorado: number;
  formaPagamento: string;
  statusPagamento: string;
  categoria: string;
  dataVencimentoParcelas: string;
  historicoPagamentos: string[];
  despesasAdicionais: number;
}

@Injectable({
  providedIn: 'root'
})
export class FinanceiroService {
  private apiUrl = 'http://localhost:8080/api/financeiro';

  constructor(private http: HttpClient) { }

  getAllFinanceiro(): Observable<Financeiro[]> {
    return this.http.get<Financeiro[]>(`${this.apiUrl}/findAll`);
  }

  getFinanceiroById(id: number): Observable<Financeiro> {
    return this.http.get<Financeiro>(`${this.apiUrl}/findById/${id}`);
  }

  createFinanceiro(financeiro: Financeiro): Observable<Financeiro> {
    return this.http.post<Financeiro>(`${this.apiUrl}/save`, financeiro);
  }

  updateFinanceiro(id: number, financeiro: Financeiro): Observable<Financeiro> {
    return this.http.put<Financeiro>(`${this.apiUrl}/update/${id}`, financeiro);
  }

  deleteFinanceiro(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  getByStatus(status: string): Observable<Financeiro[]> {
    return this.http.get<Financeiro[]>(`${this.apiUrl}/findByStatus/${status}`);
  }

  getPagamentosPendentes(): Observable<Financeiro[]> {
    return this.http.get<Financeiro[]>(`${this.apiUrl}/findByPagamentoPendente`);
  }
}