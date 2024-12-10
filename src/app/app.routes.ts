import { Routes } from '@angular/router'
import { PrincipalComponent } from './components/layout/principal/principal.component';
import { LoginComponent } from './components/layout/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AgendaListComponent } from './components/agenda-list/agenda-list.component';
import { AgendaFormComponent } from './components/agenda-form/agenda-form.component';
import { ClienteFormComponent } from './components/cliente-form/cliente-form.component'
import { ClienteListComponent } from './components/cliente-list/cliente-list.component';
import { DespesaListComponent } from './components/despesa-list/despesa-list.component';
import { DespesaFormComponent } from './components/despesa-form/despesa-form.component';
import { UsuarioListComponent } from './components/usuario-list/usuario-list.component';
import { ProcessoFormComponent } from './components/processo-form/processo-form.component';
import { ProcessoListComponent } from './components/processo-list/processo-list.component';
import { RegistrarComponent } from './components/registrar/registrar.component';
import { RecuperarSenhaComponent } from './components/recuperarsenha/recuperarsenha.component';
import { RedefinirSenhaComponent } from './components/redefinir-senha/redefinir-senha.component';
import { loginGuard } from './auth/login.guard';
export const routes: Routes = [
    {path:"", redirectTo:"login", pathMatch:"full"},
    {path:"login", component:LoginComponent},
    {path:"registrar",component:RegistrarComponent},
    {path:"recuperar-senha",component:RecuperarSenhaComponent},
    {path:"redefinir-senha",component:RedefinirSenhaComponent},

    {path:"admin", component:PrincipalComponent,canActivate:[loginGuard], children:[
        {path:"usuarios", component: UsuarioListComponent},
        {path:"clientes", component:ClienteListComponent},
        {path:"clientes/salvarCliente",component:ClienteFormComponent},
        {path:"clientes/editarCliente/:id",component:ClienteFormComponent},
        {path:"agendas", component:AgendaListComponent},
        {path:"agendas/salvarAgenda", component:AgendaFormComponent},
        {path:"agendas/editarAgenda/:id",component: AgendaFormComponent},
        {path:"despesas",component:DespesaListComponent},  
        {path:"despesas/salvarDespesa",component:DespesaFormComponent},  
        {path:"despesas/editarDespesa/:id",component:DespesaFormComponent},
        {path:"dashboard",component:DashboardComponent},
        {path:"processos",component:ProcessoListComponent},
        {path:"processos/salvarProcesso",component:ProcessoFormComponent},
        {path:"processos/editarProcesso/:id",component:ProcessoFormComponent},
    ]},
    {path:"users",component:PrincipalComponent,children:[
        {path:"clientes", component:ClienteListComponent},
        {path:"clientes/salvarCliente",component:ClienteFormComponent},
        {path:"clientes/editarCliente/:id",component:ClienteFormComponent},
        {path:"agendas", component:AgendaListComponent},
        {path:"agendas/salvarAgenda", component:AgendaFormComponent},
        {path:"agendas/editarAgenda/:id",component: AgendaFormComponent},
        {path:"dashboard",component:DashboardComponent},
    ]}
];
