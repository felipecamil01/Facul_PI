import { Component, inject } from '@angular/core';
import { MdbCollapseModule } from 'mdb-angular-ui-kit/collapse';
import { LoginService } from '../../../auth/login.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [MdbCollapseModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
loginService= inject(LoginService);
}
