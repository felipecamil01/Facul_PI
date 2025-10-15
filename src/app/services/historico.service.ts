import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
// Importe AMBAS as interfaces que definimos antes
import { HistoricoGeral, Historico } from '../models/historico.model';

@Injectable({
  providedIn: 'root'
})
export class HistoricoService {
  private http = inject(HttpClient);
  private API_URL = environment.SERVIDOR + '/api/historico';

  constructor() {}

  /**
   * Busca a lista geral com o histórico de TODAS as entidades.
   * @returns Observable<HistoricoGeral[]>
   */
  getHistoricoGeral(): Observable<HistoricoGeral[]> {
    return this.http.get<HistoricoGeral[]>(this.API_URL);
  }

  /**
   * NOVO MÉTODO: Busca o histórico de uma entidade específica.
   * @param tipoEntidade O tipo da entidade (ex: 'cliente', 'pagamentos', 'processo').
   * @param id O ID da entidade específica.
   * @returns Observable<Historico[]> - Note que retorna a interface mais simples.
   */
  getHistoricoPorEntidade(tipoEntidade: 'cliente' | 'pagamentos' | 'processo' | 'contato', id: number): Observable<Historico[]> {
    // Constrói a URL dinamicamente, por exemplo: /api/historico/pagamentos/123
    const url = `${this.API_URL}/${tipoEntidade}/${id}`;
    return this.http.get<Historico[]>(url);
  }
}