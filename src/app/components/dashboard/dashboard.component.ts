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

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.carregarClientes();
  }

  carregarClientes(): void {
    this.clienteService.findAll().subscribe(
      (clientes: Cliente[]) => {
        this.totalClientes = clientes.length;
      },
      (error: any) => { // Adicione a anotação de tipo aqui
        console.error('Erro ao carregar clientes', error);
      }
    );
  }
}

