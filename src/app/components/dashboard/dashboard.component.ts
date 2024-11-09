import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteService } from '../../services/cliente.service';
import { Financeiro, FinanceiroService } from '../../services/financeiro.service';
import { Cliente } from '../../models/cliente';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule], // Import necessário para ngFor, ngIf, etc.
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  totalClientes: number = 0;
  saudacao: string = '';
  dataAtual: string = '';
  
  // Variáveis para as contagens
  totalDespesasCategoria: Record<string, number> = {};
  statusContagens = {
    PAGO: 0,
    PENDENTE: 0,
    ATRASADO: 0
  };

  constructor(
    private clienteService: ClienteService,
    private financeiroService: FinanceiroService
  ) {}

  ngOnInit(): void {
    this.carregarClientes();
    this.carregarRegistrosFinanceiros();
    this.saudacao = this.obterSaudacao();
    this.dataAtual = this.obterDataFormatada();
  }

  carregarClientes(): void {
    this.clienteService.findAll().subscribe(
      (clientes: Cliente[]) => {
        this.totalClientes = clientes.length;
      },
      (erro: any) => {
        console.error('Erro ao carregar clientes', erro);
      }
    );
  }

  carregarRegistrosFinanceiros(): void {
    this.financeiroService.getAllFinanceiro().subscribe(
      (registros: Financeiro[]) => {
        this.contarPorCategoria(registros);
        this.contarPorStatus(registros);
      },
      (erro: any) => {
        console.error('Erro ao carregar registros financeiros', erro);
      }
    );
  }

  contarPorCategoria(registros: Financeiro[]): void {
    registros.forEach((registro) => {
      const categoria = registro.formaPagamento; // Exemplo de categoria
      if (this.totalDespesasCategoria[categoria]) {
        this.totalDespesasCategoria[categoria]++;
      } else {
        this.totalDespesasCategoria[categoria] = 1;
      }
    });
  }

  contarPorStatus(registros: Financeiro[]): void {
    registros.forEach((registro) => {
      const status = registro.statusPagamento as keyof typeof this.statusContagens;
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
    return `${this.saudacao}, hoje é ${dia} de ${mes} de ${ano}`;
  }
}
