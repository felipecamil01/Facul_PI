import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteService } from '../../../services/cliente.service';
import { DespesaService } from '../../../services/despesa.service';
import { Despesa } from '../../../models/despesa.model';
import { Cliente } from '../../../models/cliente.model';
import { NgxEchartsDirective, provideEcharts } from 'ngx-echarts';
import { EChartsOption } from 'echarts';
import { ClienteDTO } from '../../../models/ClienteDTO';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, NgxEchartsDirective],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  providers: [provideEcharts()],
})
export class DashboardComponent implements OnInit {
  totalClientes: number = 0;
  saudacao: string = '';
  dataAtual: string = '';
  horaBrasilia: string = '';

  totalDespesasCategoria: Record<string, number> = {};
  statusContagens = {
    PAGO: 0,
    PENDENTE: 0,
    ATRASADO: 0
  };

  constructor(
    private clienteService: ClienteService,
    private financeiroService: DespesaService
  ) {}

  ngOnInit(): void {
    this.carregarClientes();
    this.carregarRegistrosFinanceiros();
    this.saudacao = this.obterSaudacao();
    this.dataAtual = this.obterDataFormatada();
    this.atualizarRelogioBrasilia();
  }

  carregarClientes(): void {
    this.clienteService.findAll().subscribe(
      (clientes: ClienteDTO[]) => {
        this.totalClientes = clientes.length;
      },
      (erro: any) => {
        console.error('Erro ao carregar clientes', erro);
      }
    );
  }

  carregarRegistrosFinanceiros(): void {
    this.financeiroService.findAll().subscribe(
      (registros: Despesa[]) => {
        this.contarPorCategoria(registros);
        this.contarPorStatus(registros);
        this.atualizarGrafico();
      },
      (erro: any) => {
        console.error('Erro ao carregar registros financeiros', erro);
      }
    );
  }

  contarPorCategoria(registros: Despesa[]): void {
    registros.forEach((registro) => {
      const categoria = registro.formaPagamento; 
      if (this.totalDespesasCategoria[categoria]) {
        this.totalDespesasCategoria[categoria]++;
      } else {
        this.totalDespesasCategoria[categoria] = 1;
      }
    });
  }

  contarPorStatus(registros: Despesa[]): void {
    registros.forEach((registro) => {
      const status = registro.statusPagamento as unknown as keyof typeof this.statusContagens;
      if (this.statusContagens[status] !== undefined) {
        this.statusContagens[status]++;
      }
    });
  }

  obterSaudacao(): string {
    const agora = new Date();
    const horas = agora.getHours();
    return horas >= 5 && horas < 12 ? 'Bom dia' : horas < 18 ? 'Boa tarde' : 'Boa noite';
  }

  obterDataFormatada(): string {
    const hoje = new Date();
    const dia = hoje.getDate();
    const ano = hoje.getFullYear();
    const meses = [
      'janeiro', 'fevereiro', 'março', 'abril', 'maio', 'junho', 
      'julho', 'agosto', 'setembro', 'outubro', 'novembro', 'dezembro'
    ];
    const mes = meses[hoje.getMonth()];
    return `Hoje é ${dia} de ${mes} de ${ano}`;
  }

  atualizarRelogioBrasilia() {
    setInterval(() => {
      const options: Intl.DateTimeFormatOptions = {
        timeZone: 'America/Sao_Paulo',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
      };
      const agora = new Date().toLocaleTimeString('pt-BR', options);
      this.horaBrasilia = agora;
    }, 100);
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
