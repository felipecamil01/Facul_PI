import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { PagamentoService } from '../../../services/pagamentoService';
import { ParcelaService } from '../../../services/parcela.service';
import { Pagamento } from '../../../models/pagamento.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-pagamento-detalhe',
  standalone: true,
  imports: [CommonModule, RouterModule, CurrencyPipe, DatePipe],
  templateUrl: './pagamento-detalhe.component.html',
  styleUrls: ['./pagamento-detalhe.component.scss']
})
export class PagamentoDetalheComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private pagamentoService = inject(PagamentoService);
  private parcelaService = inject(ParcelaService);

  pagamento: Pagamento | null = null;
  id!: number;

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    if (!idStr) return;
    this.id = parseInt(idStr, 10);
    this.carregar();
  }

  carregar(): void {
    this.pagamentoService.findById(this.id).subscribe({
      next: (p) => this.pagamento = p,
      error: (e) => console.error(e)
    });
  }

  marcarParcelaComoPaga(parcela: any): void {
    if (!parcela?.id) return;
    Swal.fire({
      title: 'Confirmar pagamento?',
      text: `Marcar a parcela #${parcela.numeroParcela} como PAGA?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sim',
      cancelButtonText: 'Cancelar'
    }).then(res => {
      if (res.isConfirmed) {
        this.parcelaService.marcarComoPago(parcela.id).subscribe({
          next: () => {
            Swal.fire('Sucesso', 'Parcela marcada como paga.', 'success');
            this.carregar();
          },
          error: () => Swal.fire('Erro', 'Não foi possível marcar a parcela como paga.', 'error')
        });
      }
    });
  }
}

