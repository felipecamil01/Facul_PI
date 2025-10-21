import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Pagamento } from '../../../models/pagamento.model';
import { PagamentoService } from '../../../services/pagamentoService';
import Swal from 'sweetalert2';
import { LoginService } from '../../../auth/login.service';
import { ParcelaService } from '../../../services/parcela.service';

@Component({
  selector: 'app-pagamento-list',
  standalone: true,
  imports: [CommonModule, RouterLink, CurrencyPipe, DatePipe, FormsModule],
  templateUrl: './pagamento-list.component.html',
  styleUrls: ['./pagamento-list.component.scss']
})
export class PagamentoListComponent implements OnInit {
  loginService = inject(LoginService);
  lista: Pagamento[] = [];
  // filtros
  filtroData?: string | null = null; // yyyy-mm-dd
  filtroMes?: string | null = null; // yyyy-mm
  filtroAno?: number | null = null;
  filtroTipo: 'TODOS' | 'A_VISTA' | 'PARCELADO' = 'TODOS';
  filtroStatus: 'TODOS' | 'QUITADO' | 'PENDENTE' | 'EM_ATRASO' | 'PARCIAL' = 'TODOS';
  buscaCliente: string = '';

  showParcelasModal = false;
  selectedPagamento: Pagamento | null = null;
  pagamentoService = inject(PagamentoService);

  constructor(private router: Router, private parcelaService: ParcelaService) {}
  
  ngOnInit(): void {
    this.findAll();
  }

  aplicarFiltros(): void {
    // aplica filtros simples no frontend
    this.pagamentoService.findAll().subscribe({
      next: (lista: Pagamento[]) => {
        let res = lista;
        if (this.filtroTipo !== 'TODOS') {
          res = res.filter(p => p.tipoPagamento === this.filtroTipo);
        }
        if (this.filtroStatus !== 'TODOS') {
          res = res.filter(p => this.getStatusGeral(p) === this.filtroStatus);
        }
        if (this.filtroData) {
          res = res.filter(p => p.dataCriacao && p.dataCriacao.startsWith(this.filtroData!));
        }
        if (this.filtroMes) {
          res = res.filter(p => p.dataCriacao && p.dataCriacao.startsWith(this.filtroMes!));
        }
        if (this.filtroAno) {
          res = res.filter(p => p.dataCriacao && new Date(p.dataCriacao).getFullYear() === this.filtroAno);
        }
        this.lista = res;
      },
      error: (erro: any) => console.error(erro)
    });
  }

  limparFiltros(): void {
    this.filtroData = null;
    this.filtroMes = null;
    this.filtroAno = null;
    this.findAll();
  }

  gerarRelatorioMensal(): void {
    const year = this.filtroAno || new Date().getFullYear();
    const month = this.filtroMes ? parseInt(this.filtroMes.split('-')[1], 10) : new Date().getMonth() + 1;
    this.pagamentoService.getRelatorioMensal(year, month).subscribe({
      next: (data) => this.lista = data,
      error: (err) => console.error(err)
    });
  }

  gerarRelatorioAnual(): void {
    const year = this.filtroAno || new Date().getFullYear();
    this.pagamentoService.getRelatorioAnual(year).subscribe({
      next: (data) => this.lista = data,
      error: (err) => console.error(err)
    });
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

  onBuscarClienteChange(): void {
    const termo = (this.buscaCliente || '').trim();
    if (termo.length >= 3) {
      this.pagamentoService.searchByClienteNome(termo).subscribe({
        next: (dados) => this.lista = dados,
        error: (e) => console.error(e)
      });
    } else if (termo.length === 0) {
      this.findAll();
    }
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

  getStatusGeral(pagamento: Pagamento): 'QUITADO' | 'PENDENTE' | 'EM_ATRASO' | 'PARCIAL' {
    const parcelas = pagamento.parcelas || [];
    if (parcelas.length === 0) return 'PENDENTE';
    const total = parcelas.length;
    const pagas = parcelas.filter(p => p.statusPagamento === 'PAGO').length;
    const atrasadas = parcelas.filter(p => p.statusPagamento === 'ATRASADO').length;
    if (pagas === total) return 'QUITADO';
    if (atrasadas > 0) return 'EM_ATRASO';
    if (pagas > 0) return 'PARCIAL';
    return 'PENDENTE';
  }

  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  }

  verParcelas(pagamento: Pagamento): void {
    this.selectedPagamento = pagamento;
    this.showParcelasModal = true;
  }

  fecharParcelas(): void {
    this.showParcelasModal = false;
    this.selectedPagamento = null;
  }

  editarPagamento(pagamento: Pagamento): void {
    if (!pagamento.id) return;
    const base = this.getRoute('pagamentos/editarPagamento');
    this.router.navigate([base, pagamento.id]);
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
            if (this.selectedPagamento?.id) {
              // Recarrega o pagamento para refletir mudança
              this.pagamentoService.findById(this.selectedPagamento.id).subscribe(p => this.selectedPagamento = p);
              // Também atualiza a lista
              this.findAll();
            }
          },
          error: (e) => {
            console.error(e);
            Swal.fire('Erro', 'Não foi possível marcar a parcela como paga.', 'error');
          }
        });
      }
    });
  }
}
