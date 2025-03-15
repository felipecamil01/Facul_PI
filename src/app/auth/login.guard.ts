import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { LoginService } from './login.service';

export const loginGuard: CanActivateFn = (route, state) => {
  
  let loginService = inject(LoginService);

  if(loginService.hasPermission("ADMIN")){
    return true;
  }else{
    return false;
    
  }
};
