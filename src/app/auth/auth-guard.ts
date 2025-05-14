import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { KeycloakService } from './keycloak-service';

export const authGuard: CanActivateFn = () => {
  const keycloak = inject(KeycloakService).getKeycloak;
  return keycloak?.authenticated ?? false;
};
