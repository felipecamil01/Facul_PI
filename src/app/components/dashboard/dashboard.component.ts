import { Component, OnInit } from '@angular/core';
import { ClienteService } from '../../services/cliente.service';
import { Cliente } from '../../models/cliente';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [], 
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  totalClientes: number = 0;
  saudacao: string = '';
  dataAtual: string = '';

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.carregarClientes();
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

  obterSaudacao(): string {
    const agora = new Date();
    const horas = agora.getHours();

    if (horas >= 5 && horas < 12) {
      return 'Bom dia';
    } else if (horas >= 12 && horas < 18) {
      return 'Boa tarde';
    } else {
      return 'Boa noite';
    }
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
