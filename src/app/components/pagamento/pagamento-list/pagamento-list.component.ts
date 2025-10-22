import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import Swal from 'sweetalert2';
import { LoginService } from '../../../auth/login.service';
import { ClienteDTO } from '../../../models/ClienteDTO';
import { Pagamento } from '../../../models/pagamento.model';
import { RelatorioMensal } from '../../../models/relatorio-mensal.model';
import { PagamentoService } from '../../../services/pagamentoService';
import { ParcelaService } from '../../../services/parcela.service';
import { ClienteService } from '../../../services/cliente.service';

type StatusFiltroLista = 'TODOS' | 'QUITADO' | 'PENDENTE' | 'EM_ATRASO' | 'PARCIAL';
type StatusFiltroRelatorio = 'TODOS' | 'PAGO' | 'ATRASADO' | 'PENDENTE';

@Component({
  selector: 'app-pagamento-list',
  standalone: true,
  imports: [CommonModule, RouterLink, CurrencyPipe, DatePipe, FormsModule],
  templateUrl: './pagamento-list.component.html',
  styleUrls: ['./pagamento-list.component.scss']
})
export class PagamentoListComponent implements OnInit {
  loginService = inject(LoginService);
  pagamentoService = inject(PagamentoService);

  lista: Pagamento[] = [];
  filtroData?: string | null = null;
  filtroMes?: string | null = null;
  filtroAno?: number | null = null;
  filtroTipo: 'TODOS' | 'A_VISTA' | 'PARCELADO' = 'TODOS';
  filtroStatus: StatusFiltroLista = 'TODOS';
  buscaCliente: string = '';

  showParcelasModal = false;
  selectedPagamento: Pagamento | null = null;

  clientes: ClienteDTO[] = [];
  clienteFiltroNome = '';
  clienteFiltroId: number | null = null;
  filtroStatusRelatorio: StatusFiltroRelatorio = 'TODOS';
  relatorioMensal?: RelatorioMensal;
  gerandoRelatorio = false;

  constructor(
    private router: Router,
    private parcelaService: ParcelaService,
    private clienteService: ClienteService
  ) {}

  ngOnInit(): void {
    this.findAll();
    this.carregarClientes();
  }

  private carregarClientes(): void {
    this.clienteService.findAll().subscribe({
      next: (clientes) => (this.clientes = clientes),
      error: (e) => console.error('Erro ao carregar clientes para filtro', e)
    });
  }

  aplicarFiltros(): void {
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
    this.filtroStatus = 'TODOS';
    this.filtroTipo = 'TODOS';
    this.findAll();
  }

  gerarRelatorioMensal(showFeedback = true): void {
    const ano = this.filtroAno || new Date().getFullYear();
    const mes = this.filtroMes ? parseInt(this.filtroMes.split('-')[1], 10) : new Date().getMonth() + 1;
    const filtros = {
      clienteId: this.clienteFiltroId ?? undefined,
      status: this.filtroStatusRelatorio === 'TODOS' ? undefined : this.filtroStatusRelatorio
    };

    this.gerandoRelatorio = true;
    this.pagamentoService.getRelatorioMensal(ano, mes, filtros).subscribe({
      next: (relatorio) => {
        this.relatorioMensal = relatorio;
        this.gerandoRelatorio = false;
        if (showFeedback) {
          Swal.fire('Relatório pronto', 'Os dados do mês foram carregados.', 'success');
        }
      },
      error: (err) => {
        this.gerandoRelatorio = false;
        console.error(err);
        Swal.fire('Erro', 'Não foi possível gerar o relatório mensal.', 'error');
      }
    });
  }

  baixarRelatorio(formato: 'pdf' | 'csv'): void {
    if (!this.relatorioMensal) {
      Swal.fire('Atenção', 'Gere o relatório mensal antes de baixar o arquivo.', 'info');
      return;
    }
    const ano = this.relatorioMensal.ano;
    const mes = this.relatorioMensal.mes;
    const filtros = {
      clienteId: this.relatorioMensal.clienteId ?? undefined,
      status: this.relatorioMensal.filtroStatus
    };
    this.pagamentoService.downloadRelatorioMensal(formato, ano, mes, filtros).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `relatorio-mensal-${ano}-${String(mes).padStart(2, '0')}.${formato}`;
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => Swal.fire('Erro', 'Não foi possível baixar o arquivo solicitado.', 'error')
    });
  }

  gerarRelatorioAnual(): void {
    const year = this.filtroAno || new Date().getFullYear();
    this.pagamentoService.getRelatorioAnual(year).subscribe({
      next: (data) => this.lista = data,
      error: (err) => console.error(err)
    });
  }

  findAll(): void {
    this.pagamentoService.findAll().subscribe({
      next: (lista: Pagamento[]) => {
        this.lista = lista;
      },
      error: (erro: any) => {
        console.error('Ocorreu um erro:', erro);
        Swal.fire('Erro!', 'Não foi possível carregar a lista de pagamentos.', 'error');
      }
    });
  }

  delete(pagamento: Pagamento): void {
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
            this.findAll();
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

  atualizarFiltroCliente(): void {
    const encontrado = this.clientes.find(cliente => cliente.nome === this.clienteFiltroNome.trim());
    this.clienteFiltroId = encontrado ? Number(encontrado.id) : null;
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

  getStatusGeral(pagamento: Pagamento): StatusFiltroLista {
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

  confirmarPagamento(pagamento: Pagamento): void {
    const pagamentoId = pagamento?.id;
    if (!pagamentoId) return;
    Swal.fire({
      title: 'Confirmar pagamento?',
      text: 'Todas as parcelas serão marcadas como pagas e as pendências serão atualizadas.',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sim',
      cancelButtonText: 'Cancelar'
    }).then(resposta => {
      if (resposta.isConfirmed) {
        this.pagamentoService.confirmarPagamento(pagamentoId).subscribe({
          next: () => {
            Swal.fire('Sucesso', 'Pagamento confirmado automaticamente.', 'success');
            this.findAll();
            if (this.relatorioMensal) {
              this.gerarRelatorioMensal(false);
            }
          },
          error: () => Swal.fire('Erro', 'Não foi possível confirmar o pagamento.', 'error')
        });
      }
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
            if (this.selectedPagamento?.id) {
              this.pagamentoService.findById(this.selectedPagamento.id).subscribe(p => this.selectedPagamento = p);
            }
            this.findAll();
            if (this.relatorioMensal) {
              this.gerarRelatorioMensal(false);
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
