import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { LoginService } from './login.service';

export const loginGuard: CanActivateFn = (route, state) => {
  
  let loginService = inject(LoginService);

  if(loginService.hasPermission("user") && state.url=='/admin/despesa'){
    return false;
    
  }
  
  return true;
};
