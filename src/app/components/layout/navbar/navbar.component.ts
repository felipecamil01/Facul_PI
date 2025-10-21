import { Component, HostListener, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MdbCollapseModule } from 'mdb-angular-ui-kit/collapse';
import { LoginService } from '../../../auth/login.service';
import { AgendaService } from '../../../services/agenda.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, MdbCollapseModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent implements OnInit {
  loginService = inject(LoginService);
  private agendaService = inject(AgendaService);
  notificacoes: any[] = [];
  showNotificacoes = false;

  ngOnInit(): void {
    // Busca eventos nos próximos 7 dias
    this.agendaService.findAll().subscribe({
      next: (agendas: any[]) => {
        const hoje = new Date();
        const limite = new Date();
        limite.setDate(limite.getDate() + 7);
        this.notificacoes = agendas.filter(a => {
          const d = new Date(a.data);
          return d >= hoje && d <= limite;
        }).slice(0, 10);
      },
      error: () => {}
    });
  }

  getRoute(path: string): string {
    return this.loginService.hasPermission('ADMIN') ? `/admin/${path}` : `/user/${path}`;
  }

  toggleNotificacoes(ev: Event): void {
    ev.stopPropagation();
    this.showNotificacoes = !this.showNotificacoes;
  }

  @HostListener('document:click')
  closeNotificacoes(): void {
    this.showNotificacoes = false;
  }
}
