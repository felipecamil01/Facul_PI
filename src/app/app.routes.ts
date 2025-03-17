import { Routes } from '@angular/router'
import { PrincipalComponent } from './components/layout/principal/principal.component';
import { DashboardComponent } from './components/layout/dashboard/dashboard.component';
import { AgendaListComponent } from './components/agenda/agenda-list/agenda-list.component';
import { AgendaFormComponent } from './components/agenda/agenda-form/agenda-form.component';
import { ClienteFormComponent } from './components/cliente/cliente-form/cliente-form.component'
import { ClienteListComponent } from './components/cliente/cliente-list/cliente-list.component';
import { DespesaListComponent } from './components/despesa/despesa-list/despesa-list.component';
import { DespesaFormComponent } from './components/despesa/despesa-form/despesa-form.component';
import { ProcessoFormComponent } from './components/processo/processo-form/processo-form.component';
import { ProcessoListComponent } from './components/processo/processo-list/processo-list.component';
import { LoginComponent } from './components/autenticacao/login/login.component';
import { RegistrarComponent } from './components/autenticacao/registrar/registrar.component';
import { RecuperarSenhaComponent } from './components/autenticacao/recuperarsenha/recuperarsenha.component';
import { loginGuard } from './auth/login.guard';
export const routes: Routes = [
    {path:"", redirectTo:"login", pathMatch:"full"},
    {path:"login", component:LoginComponent},
    {path:"registrar",component:RegistrarComponent},
    {path:"recuperar-senha",component:RecuperarSenhaComponent},

    {path:"admin", component:PrincipalComponent,canActivate:[loginGuard], children:[
        {path:"cliente", component:ClienteListComponent},
        {path:"cliente/salvarCliente",component:ClienteFormComponent},
        {path:"cliente/editarCliente/:id",component:ClienteFormComponent},
        {path:"agenda", component:AgendaListComponent},
        {path:"agenda/salvarAgenda", component:AgendaFormComponent},
        {path:"agenda/editarAgenda/:id",component: AgendaFormComponent},
        {path:"despesa",component:DespesaListComponent},  
        {path:"despesa/salvarDespesa",component:DespesaFormComponent},  
        {path:"despesa/editarDespesa/:id",component:DespesaFormComponent},
        {path:"dashboard",component:DashboardComponent},
        {path:"processo",component:ProcessoListComponent},
        {path:"processo/salvarProcesso",component:ProcessoFormComponent},
        {path:"processo/editarProcesso/:id",component:ProcessoFormComponent},
    ]},
    {path:"user",component:PrincipalComponent,children:[
        {path:"cliente", component:ClienteListComponent},
        {path:"cliente/salvarCliente",component:ClienteFormComponent},
        {path:"cliente/editarCliente/:id",component:ClienteFormComponent},
        {path:"agenda", component:AgendaListComponent},
        {path:"agenda/salvarAgenda", component:AgendaFormComponent},
        {path:"agenda/editarAgenda/:id",component: AgendaFormComponent},
        {path:"dashboard",component:DashboardComponent},
    ]}
];
