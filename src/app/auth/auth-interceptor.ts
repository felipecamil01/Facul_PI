import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { KeycloakService } from './keycloak-service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(KeycloakService).getKeycloak?.token;

  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  return next(req);
};
