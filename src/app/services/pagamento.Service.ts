import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pagamento } from '../models/pagamento.model';

@Injectable({
  providedIn: 'root'
})
export class PagamentoService {
  // ATENÇÃO: Verifique se a URL da sua API está correta.
  private apiUrl = 'http://localhost:8080/api/pagamento';

  constructor(private http: HttpClient) { }

  /**
   * Busca a lista completa de pagamentos.
   */
  findAll(): Observable<Pagamento[]> {
    return this.http.get<Pagamento[]>(`${this.apiUrl}/findAll`);
  }

  /**
   * Busca um pagamento específico pelo seu ID.
   */
  findById(id: number): Observable<Pagamento> {
    return this.http.get<Pagamento>(`${this.apiUrl}/findById/${id}`);
  }

  /**
   * Salva um novo registro de pagamento.
   */
  save(pagamento: Pagamento): Observable<Pagamento> {
    return this.http.post<Pagamento>(`${this.apiUrl}/save`, pagamento);
  }

  /**
   * Atualiza um pagamento existente.
   */
  update(id: number, pagamento: Pagamento): Observable<Pagamento> {
    return this.http.put<Pagamento>(`${this.apiUrl}/update/${id}`, pagamento);
  }

  /**
   * Deleta um pagamento pelo seu ID.
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  // --- Métodos para Relatórios (Exemplos) ---

  /**
   * Busca o relatório de pagamentos por cliente.
   */
  getRelatorioPorCliente(clienteId: number): Observable<Pagamento[]> {
    return this.http.get<Pagamento[]>(`${this.apiUrl}/relatorio/cliente/${clienteId}`);
  }

  /**
   * Busca o relatório mensal de pagamentos.
   */
  getRelatorioMensal(ano: number, mes: number): Observable<Pagamento[]> {
    return this.http.get<Pagamento[]>(`${this.apiUrl}/relatorio/mensal?ano=${ano}&mes=${mes}`);
  }

  /**
   * Busca o relatório anual de pagamentos.
   */
  getRelatorioAnual(ano: number): Observable<Pagamento[]> {
    return this.http.get<Pagamento[]>(`${this.apiUrl}/relatorio/anual?ano=${ano}`);
  }
}