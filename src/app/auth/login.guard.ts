import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from './login.service';

export const loginGuard: CanActivateFn = () => {
  const loginService = inject(LoginService);
  const router = inject(Router);

  if (!loginService.getToken()) {
    router.navigate(['/login']);
    return false;
  }

  if (!loginService.isAdmin()) {
    router.navigate(['/user/dashboard']);
    return false;
  }

  return true;
};
