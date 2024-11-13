import { Routes } from '@angular/router';
import { UsuarioListComponent } from './components/usuario-list/usuario-list.component';
import { ClienteListComponent } from './components/cliente-list/cliente-list.component';
import { ClienteSaveComponent } from './components/cliente-save/cliente-save.component';
import { ClienteEditarComponent } from './components/cliente-editar/cliente-editar.component';
import { PrincipalComponent } from './components/layout/principal/principal.component';
import { LoginComponent } from './components/layout/login/login.component';
import { AgendaListComponent } from './components/agenda-list/agenda-list.component';
import { DespesaListComponent } from './components/despesa-list/despesa-list.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ProcessoComponent } from './components/processo/processo.component';
import { DespesaSaveComponent } from './components/despesa-save/despesa-save.component';
import { AgendaSaveComponent } from './components/agenda-save/agenda-save.component';

export const routes: Routes = [
    {path:"", redirectTo:"login", pathMatch:"full"},
    {path:"login", component:LoginComponent},
    {path:"principal", component:PrincipalComponent, children:[
        {path:"usuarios", component: UsuarioListComponent},
        {path:"clientes", component:ClienteListComponent},
        {path:"clientes/salvarCliente",component:ClienteSaveComponent},
        {path:"clientes/editarCliente/:id",component:ClienteEditarComponent},
        {path:"agendas", component:AgendaListComponent},
        {path:"agendas/salvarAgenda", component:AgendaSaveComponent},
        {path:"agendas/salvarAgenda/:id",component: AgendaSaveComponent},
        {path:"despesas",component:DespesaListComponent},  
        {path:"despesas/salvarDespesa",component:DespesaSaveComponent},  
        {path:"despesas/salvarDespesa/:id",component:DespesaSaveComponent},
        {path:"dashboard",component:DashboardComponent},
        {path:"processo",component:ProcessoComponent}
    ]},
];
