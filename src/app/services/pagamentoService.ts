import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pagamento } from '../models/pagamento.model';
import { RelatorioMensal } from '../models/relatorio-mensal.model';
import { StatusPagamento } from '../models/status-pagamento.type';

interface RelatorioMensalFiltros {
  clienteId?: number;
  status?: StatusPagamento;
}

@Injectable({
  providedIn: 'root'
})
export class PagamentoService {
  private apiUrl = 'http://localhost:8080/api/pagamento';

  constructor(private http: HttpClient) {}

  findAll(): Observable<Pagamento[]> {
    return this.http.get<Pagamento[]>(`${this.apiUrl}/findAll`);
  }

  findById(id: number): Observable<Pagamento> {
    return this.http.get<Pagamento>(`${this.apiUrl}/findById/${id}`);
  }

  save(pagamento: Pagamento): Observable<Pagamento> {
    return this.http.post<Pagamento>(`${this.apiUrl}/save`, pagamento);
  }

  update(id: number, pagamento: Pagamento): Observable<Pagamento> {
    return this.http.put<Pagamento>(`${this.apiUrl}/update/${id}`, pagamento);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  confirmarPagamento(id: number, dataPagamento?: string): Observable<Pagamento> {
    let params = new HttpParams();
    if (dataPagamento) {
      params = params.set('dataPagamento', dataPagamento);
    }
    return this.http.put<Pagamento>(`${this.apiUrl}/${id}/confirmar`, {}, { params });
  }

  getRelatorioPorCliente(clienteId: number): Observable<Pagamento[]> {
    return this.http.get<Pagamento[]>(`${this.apiUrl}/relatorio/cliente/${clienteId}`);
  }

  getRelatorioMensal(ano: number, mes: number, filtros?: RelatorioMensalFiltros): Observable<RelatorioMensal> {
    let params = new HttpParams().set('ano', String(ano)).set('mes', String(mes));
    if (filtros?.clienteId) {
      params = params.set('clienteId', String(filtros.clienteId));
    }
    if (filtros?.status) {
      params = params.set('status', filtros.status);
    }
    return this.http.get<RelatorioMensal>(`${this.apiUrl}/relatorio/mensal`, { params });
  }

  downloadRelatorioMensal(formato: 'pdf' | 'csv',
                          ano: number,
                          mes: number,
                          filtros?: RelatorioMensalFiltros): Observable<Blob> {
    let params = new HttpParams()
      .set('ano', String(ano))
      .set('mes', String(mes))
      .set('formato', formato);

    if (filtros?.clienteId) {
      params = params.set('clienteId', String(filtros.clienteId));
    }
    if (filtros?.status) {
      params = params.set('status', filtros.status);
    }

    return this.http.get(`${this.apiUrl}/relatorio/mensal/export`, {
      params,
      responseType: 'blob'
    });
  }

  getRelatorioAnual(ano: number): Observable<Pagamento[]> {
    return this.http.get<Pagamento[]>(`${this.apiUrl}/relatorio/anual?ano=${ano}`);
  }

  searchByClienteNome(nome: string): Observable<Pagamento[]> {
    return this.http.get<Pagamento[]>(`${this.apiUrl}/search?nome=${encodeURIComponent(nome)}`);
  }
}
