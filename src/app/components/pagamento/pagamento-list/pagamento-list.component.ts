import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Pagamento } from '../../../models/pagamento.model';
import { PagamentoService } from '../../../services/pagamento.Service';
import Swal from 'sweetalert2';
import { LoginService } from '../../../auth/login.service';

@Component({
  selector: 'app-pagamento-list',
  standalone: true,
  imports: [CommonModule, RouterLink, CurrencyPipe, DatePipe],
  templateUrl: './pagamento-list.component.html',
  styleUrls: ['./pagamento-list.component.scss']
})
export class PagamentoListComponent implements OnInit {
  loginService = inject(LoginService);
  lista: Pagamento[] = [];
  pagamentoService = inject(PagamentoService);

  constructor(private router: Router) {}
  
  ngOnInit(): void {
    this.findAll();
  }

  findAll() {
    this.pagamentoService.findAll().subscribe({
      next: (lista: Pagamento[]) => {
        this.lista = lista;
      },
      error: (erro: any) => {
        console.error('Ocorreu um erro:', erro);
        Swal.fire('Erro!', 'Não foi possível carregar a lista de pagamentos.', 'error');
      },
    });
  }

  delete(pagamento: Pagamento) {
    Swal.fire({
      title: 'Tem certeza?',
      text: `Deseja deletar o registro de pagamento para ${pagamento.cliente.nome}? Esta ação não pode ser desfeita.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sim, deletar!',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.pagamentoService.delete(pagamento.id!).subscribe({
          next: () => {
            Swal.fire('Deletado!', 'O registro foi removido com sucesso.', 'success');
            this.findAll(); // Recarrega a lista
          },
          error: (err: any) => {
            Swal.fire('Erro!', 'Não foi possível deletar o registro.', 'error');
            console.error(err);
          }
        });
      }
    });
  }

  getProgresso(pagamento: Pagamento): string {
    if (!pagamento.parcelas || pagamento.parcelas.length === 0) {
      return 'N/A';
    }
    const pagas = pagamento.parcelas.filter(p => p.statusPagamento === 'PAGO').length;
    return `${pagas} / ${pagamento.parcelas.length}`;
  }

  getProgressoBadge(pagamento: Pagamento): string {
    if (!pagamento.parcelas || pagamento.parcelas.length === 0) return 'bg-secondary';
    
    const pagas = pagamento.parcelas.filter(p => p.statusPagamento === 'PAGO').length;
    const total = pagamento.parcelas.length;

    if (pagas === total) return 'bg-success';
    if (pagamento.parcelas.some(p => p.statusPagamento === 'ATRASADO')) return 'bg-danger';
    if (pagas > 0) return 'bg-primary'; 
    
    return 'bg-warning text-dark';
  }

  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  }
}