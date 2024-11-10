import { Routes } from '@angular/router';
import { UsuarioListComponent } from './components/usuario-list/usuario-list.component';
import { ClienteListComponent } from './components/cliente-list/cliente-list.component';
import { ClienteSaveComponent } from './components/cliente-save/cliente-save.component';
import { ClienteEditarComponent } from './components/cliente-editar/cliente-editar.component';
import { PrincipalComponent } from './components/layout/principal/principal.component';
import { LoginComponent } from './components/layout/login/login.component';
import { AgendaComponent } from './components/agenda/agenda.component';
import { FinanceiroComponent } from './components/financeiro/financeiro.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ProcessoComponent } from './components/processo/processo.component';

export const routes: Routes = [
    {path:"", redirectTo:"login", pathMatch:"full"},
    {path:"login", component:LoginComponent},
    {path:"principal", component:PrincipalComponent, children:[
        {path:"usuarios", component: UsuarioListComponent},
        {path:"clientes", component:ClienteListComponent},
        {path:"clientes/salvarCliente",component:ClienteSaveComponent},
        {path:"clientes/editarCliente/:id",component:ClienteEditarComponent},
        {path:"Agenda",component:AgendaComponent},
        {path:"financeiro",component:FinanceiroComponent},  
        {path:"dashboard",component:DashboardComponent},
        {path:"processo",component:ProcessoComponent}
    ]},
];
