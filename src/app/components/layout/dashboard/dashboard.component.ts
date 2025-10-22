import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteService } from '../../../services/cliente.service';
import { AgendaService } from '../../../services/agenda.service';
import { PagamentoService } from '../../../services/pagamentoService';
import { NgxEchartsDirective, provideEcharts } from 'ngx-echarts';
import { EChartsOption } from 'echarts';
import { ClienteDTO } from '../../../models/ClienteDTO';
import { Pagamento } from '../../../models/pagamento.model';

interface PrazoDashboard {
  descricao: string;
  data: Date;
  prioridade?: string | null;
  cliente?: string | null;
  processo?: string | null;
  diasRestantes: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, NgxEchartsDirective],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  providers: [provideEcharts()]
})
export class DashboardComponent implements OnInit, OnDestroy {
  totalClientes = 0;
  saudacao = '';
  dataAtual = '';
  horaBrasilia = '';

  totalPagamentosCategoria: Record<string, number> = {};
  statusContagens = {
    PAGO: 0,
    PENDENTE: 0,
    ATRASADO: 0
  };

  prazosProximos: PrazoDashboard[] = [];

  private relogioTimer?: ReturnType<typeof setInterval>;

  constructor(
    private clienteService: ClienteService,
    private financeiroService: PagamentoService,
    private agendaService: AgendaService
  ) {}

  ngOnInit(): void {
    this.carregarClientes();
    this.carregarRegistrosFinanceiros();
    this.carregarPrazosImportantes();
    this.saudacao = this.obterSaudacao();
    this.dataAtual = this.obterDataFormatada();
    this.iniciarRelogioBrasilia();
  }

  ngOnDestroy(): void {
    if (this.relogioTimer) {
      clearInterval(this.relogioTimer);
    }
  }

  carregarClientes(): void {
    this.clienteService.findAll().subscribe({
      next: (clientes: ClienteDTO[]) => (this.totalClientes = clientes.length),
      error: (erro) => console.error('Erro ao carregar clientes', erro)
    });
  }

  carregarRegistrosFinanceiros(): void {
    this.financeiroService.findAll().subscribe({
      next: (registros: Pagamento[]) => {
        this.contarPorCategoria(registros);
        this.contarPorStatus(registros);
        this.atualizarGrafico();
      },
      error: (erro) => console.error('Erro ao carregar registros financeiros', erro)
    });
  }

  carregarPrazosImportantes(): void {
    this.agendaService.findAll().subscribe({
      next: (eventos: any[]) => {
        const agora = new Date();
        this.prazosProximos = eventos
          .filter((evento) => evento.prazoImportante || evento.tipo === 'PRAZO_IMPORTANTE')
          .map((evento) => {
            const dataEvento = new Date(evento.data);
            const diff = dataEvento.getTime() - agora.getTime();
            const diasRestantes = Math.ceil(diff / (1000 * 60 * 60 * 24));
            return {
              descricao: evento.descricao || evento.titulo || 'Prazo importante',
              data: dataEvento,
              prioridade: evento.prioridade,
              cliente: evento.cliente?.nome || evento.processo?.cliente?.nome || null,
              processo: evento.processo?.numeroProcesso || null,
              diasRestantes
            } as PrazoDashboard;
          })
          .sort((a, b) => a.data.getTime() - b.data.getTime())
          .slice(0, 6);
      },
      error: (erro) => console.error('Erro ao carregar prazos importantes', erro)
    });
  }

  contarPorCategoria(registros: Pagamento[]): void {
    registros.forEach((registro) => {
      const categoriaBruta = (registro.formaPagamento || registro.tipoPagamento || 'OUTROS') as string;
      const categoria = categoriaBruta.toString();
      this.totalPagamentosCategoria[categoria] = (this.totalPagamentosCategoria[categoria] ?? 0) + 1;
    });
  }

  contarPorStatus(registros: Pagamento[]): void {
    registros.forEach((registro) => {
      const status = registro.statusPagamento as keyof typeof this.statusContagens;
      if (this.statusContagens[status] !== undefined) {
        this.statusContagens[status]++;
      }
    });
  }

  obterSaudacao(): string {
    const horas = new Date().getHours();
    return horas >= 5 && horas < 12 ? 'Bom dia' : horas < 18 ? 'Boa tarde' : 'Boa noite';
  }

  obterDataFormatada(): string {
    return new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric'
    }).format(new Date());
  }

  iniciarRelogioBrasilia(): void {
    this.atualizarRelogioBrasilia();
    this.relogioTimer = setInterval(() => this.atualizarRelogioBrasilia(), 1000);
  }

  atualizarRelogioBrasilia(): void {
    const options: Intl.DateTimeFormatOptions = {
      timeZone: 'America/Sao_Paulo',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    };
    this.horaBrasilia = new Date().toLocaleTimeString('pt-BR', options);
  }

  atualizarGrafico(): void {
    const series = this.chartOption.series as { data: { value: number; name: string }[] }[];
    if (series.length > 0) {
      series[0].data = [
        { value: this.statusContagens.PENDENTE, name: 'Pendentes' },
        { value: this.statusContagens.PAGO, name: 'Pagas' },
        { value: this.statusContagens.ATRASADO, name: 'Atrasadas' }
      ];
    }
  }

  chartOption: EChartsOption = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: ['Pendentes', 'Pagas', 'Atrasadas']
    },
    series: [
      {
        name: 'Status de Pagamento',
        type: 'pie',
        radius: '50%',
        data: [
          { value: this.statusContagens.PENDENTE, name: 'Pendentes' },
          { value: this.statusContagens.PAGO, name: 'Pagas' },
          { value: this.statusContagens.ATRASADO, name: 'Atrasadas' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        itemStyle: {
          color: (params) => {
            const colors = ['#FFD700', '#228B22', '#B22222'];
            return colors[params.dataIndex];
          }
        }
      }
    ]
  };
}

