import { Routes } from '@angular/router';
import { PrincipalComponent } from './components/layout/principal/principal.component';
import { DashboardComponent } from './components/layout/dashboard/dashboard.component';
import { AgendaListComponent } from './components/agenda/agenda-list/agenda-list.component';
import { AgendaFormComponent } from './components/agenda/agenda-form/agenda-form.component';
import { ClienteFormComponent } from './components/cliente/cliente-form/cliente-form.component';
import { ClienteListComponent } from './components/cliente/cliente-list/cliente-list.component';
import { DespesaListComponent } from './components/despesa/despesa-list/despesa-list.component';
import { DespesaFormComponent } from './components/despesa/despesa-form/despesa-form.component';
import { ProcessoFormComponent } from './components/processo/processo-form/processo-form.component';
import { ProcessoListComponent } from './components/processo/processo-list/processo-list.component';
import { authGuard } from './auth/auth-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'admin/dashboard', pathMatch: 'full' },
  {
    path: 'admin',
    component: PrincipalComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'cliente', component: ClienteListComponent },
      { path: 'cliente/salvarCliente', component: ClienteFormComponent },
      { path: 'cliente/editarCliente/:id', component: ClienteFormComponent },
      { path: 'agenda', component: AgendaListComponent },
      { path: 'agenda/salvarAgenda', component: AgendaFormComponent },
      { path: 'agenda/editarAgenda/:id', component: AgendaFormComponent },
      { path: 'despesa', component: DespesaListComponent },
      { path: 'despesa/salvarDespesa', component: DespesaFormComponent },
      { path: 'despesa/editarDespesa/:id', component: DespesaFormComponent },
      { path: 'processo', component: ProcessoListComponent },
      { path: 'processo/salvarProcesso', component: ProcessoFormComponent },
      { path: 'processo/editarProcesso/:id', component: ProcessoFormComponent },
    ],
  },
  {
    path: 'user',
    component: PrincipalComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'cliente', component: ClienteListComponent },
      { path: 'cliente/salvarCliente', component: ClienteFormComponent },
      { path: 'cliente/editarCliente/:id', component: ClienteFormComponent },
      { path: 'agenda', component: AgendaListComponent },
      { path: 'agenda/salvarAgenda', component: AgendaFormComponent },
      { path: 'agenda/editarAgenda/:id', component: AgendaFormComponent },
    ],
  },
];