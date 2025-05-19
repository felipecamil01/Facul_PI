import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';
import { UserProfile } from './user-profile';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private keycloak: Keycloak | undefined;
  private profile: UserProfile | undefined;

  get getKeycloak() {
    if (!this.keycloak) {
      this.keycloak = new Keycloak({
        url: 'https://backend.fisio.lan:7443/',
        realm: 'main_realm',
        clientId: 'lionlaw',
      });
    }
    return this.keycloak;
  }

  get getProfile() {
    return this.profile;
  }

  getRoute(path: string): string {
    if (this.profile && this.profile.role === 'ADMIN') {
      return `/admin/${path}`;
    } else {
      return `/user/${path}`;
    }
  }

  async init() {
  console.log('Autenticando Usuário');
  const authenticated = await this.getKeycloak?.init({
    onLoad: 'login-required',
  });

  if (authenticated) {
    console.log('Usuário autenticado');
    this.profile = (await this.keycloak?.loadUserProfile() as UserProfile);
    this.profile.token = this.keycloak?.token;

    const clientRoles = this.keycloak?.resourceAccess?.['llw']?.roles;

    if (clientRoles?.includes('ADMIN')) {
      this.profile.role = 'ADMIN';
    } else {
      this.profile.role = 'USER';
    }
  }
}

  login() {
    return this.keycloak?.login();
  }

  logout() {
    return this.keycloak?.logout();
  }
}
