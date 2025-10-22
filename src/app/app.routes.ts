import { Routes } from '@angular/router'
import { PrincipalComponent } from './components/layout/principal/principal.component';
import { DashboardComponent } from './components/layout/dashboard/dashboard.component';
import { AgendaListComponent } from './components/agenda/agenda-list/agenda-list.component';
import { AgendaFormComponent } from './components/agenda/agenda-form/agenda-form.component';
import { ClienteFormComponent } from './components/cliente/cliente-form/cliente-form.component'
import { ClienteListComponent } from './components/cliente/cliente-list/cliente-list.component';
import { ProcessoFormComponent } from './components/processo/processo-form/processo-form.component';
import { ProcessoListComponent } from './components/processo/processo-list/processo-list.component';
import { LoginComponent } from './components/autenticacao/login/login.component';
import { RegistrarComponent } from './components/autenticacao/registrar/registrar.component';
import { RecuperarSenhaComponent } from './components/autenticacao/recuperarsenha/recuperarsenha.component';
import { RedefinirSenhaComponent } from './components/autenticacao/redefinir-senha/redefinir-senha.component';
import { PagamentoListComponent } from './components/pagamento/pagamento-list/pagamento-list.component';
import { PagamentoFormComponent } from './components/pagamento/pagamento-form/pagamento-form.component';
import { PagamentoDetalheComponent } from './components/pagamento/pagamento-detalhe/pagamento-detalhe.component';
import { loginGuard } from './auth/login.guard';
import { authGuard } from './auth/auth.guard';
import { HistoricoComponent } from './components/historico/historico-list/historico-list.component';
import { DocumentoListComponent } from './components/documento/documento-list/documento-list.component';
export const routes: Routes = [
    {path:"", redirectTo:"login", pathMatch:"full"},
    {path:"login", component:LoginComponent},
    {path:"registrar",component:RegistrarComponent},
    {path:"recuperar-senha",component:RecuperarSenhaComponent},
    {path:"redefinir-senha",component:RedefinirSenhaComponent},

    {path:"admin", component:PrincipalComponent,canActivate:[loginGuard], children:[
        {path:"dashboard",component:DashboardComponent},
        {path:"cliente", component:ClienteListComponent},
        {path:"cliente/salvarCliente",component:ClienteFormComponent},
        {path:"cliente/editarCliente/:id",component:ClienteFormComponent},
        {path:"agenda", component:AgendaListComponent},
        {path:"agenda/salvarAgenda", component:AgendaFormComponent},
        {path:"agenda/editarAgenda/:id",component: AgendaFormComponent},
        {path:"pagamentos",component:PagamentoListComponent},  
        {path:"pagamentos/salvarPagamento",component:PagamentoFormComponent},  
        {path:"pagamentos/editarPagamento/:id",component:PagamentoFormComponent},
        {path:"pagamentos/detalhe/:id",component:PagamentoDetalheComponent},
        {path:"processo",component:ProcessoListComponent},
        {path:"processo/salvarProcesso",component:ProcessoFormComponent},
        {path:"processo/editarProcesso/:id",component:ProcessoFormComponent},
        {path:"documento", component: DocumentoListComponent},
        {path:"historico",component:HistoricoComponent}
    ]},
    {path:"user",component:PrincipalComponent,canActivate:[authGuard],children:[
        {path:"dashboard",component:DashboardComponent},
        {path:"cliente", component:ClienteListComponent},
        {path:"cliente/salvarCliente",component:ClienteFormComponent},
        {path:"cliente/editarCliente/:id",component:ClienteFormComponent},
        {path:"agenda", component:AgendaListComponent},
        {path:"agenda/salvarAgenda", component:AgendaFormComponent},
        {path:"agenda/editarAgenda/:id",component: AgendaFormComponent},
        {path:"pagamentos/detalhe/:id",component:PagamentoDetalheComponent},
        {path:"documento", component: DocumentoListComponent},
    ]}
];
