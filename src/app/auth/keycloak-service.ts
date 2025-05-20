import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';
import { UserProfile } from './user-profile';
import { environment } from '../../../src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private keycloak: Keycloak | undefined;
  private profile: UserProfile | undefined;

  get getKeycloak() {
    if (!this.keycloak) {
      this.keycloak = new Keycloak({
        url: environment.keycloakUrl,
        realm: environment.keycloakRealm,
        clientId: environment.keycloakClientId,
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
      checkLoginIframe: false,
      flow: 'standard',
    });

    if (authenticated) {
      console.log('Usuário autenticado');

      this.profile = (await this.keycloak?.loadUserProfile() as UserProfile);
      this.profile.token = this.keycloak?.token;

      const realmRoles = this.keycloak?.realmAccess?.roles || [];
      const clientRoles = this.keycloak?.resourceAccess?.[environment.keycloakClientId]?.roles || [];

      // Se tiver a client role 'led_only', bloqueia acesso
      if (clientRoles.includes('led_only')) {
        console.error('Acesso negado: usuário com role led_only não pode acessar este sistema.');
        await this.logout();
        throw new Error('Acesso negado: sem permissão para este sistema.');
      }

      // Se tiver realm role admin, define ADMIN
      if (realmRoles.includes('ADMIN')) {
        this.profile.role = 'ADMIN';
      } 
      // Se tiver realm role user, define USER
      else{
        this.profile.role = 'USER';
      } 
      

    } else {
      console.error('Usuário não autenticado');
      throw new Error('Falha na autenticação');
    }
  }

  login() {
    return this.keycloak?.login();
  }

  logout() {
    return this.keycloak?.logout();
  }
}