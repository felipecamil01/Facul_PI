import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ParcelaService {
  private http = inject(HttpClient);
  private API = environment.SERVIDOR + '/api/parcela';

  marcarComoPago(id: number): Observable<any> {
    return this.http.put<any>(`${this.API}/pagar/${id}`, {});
  }
}

