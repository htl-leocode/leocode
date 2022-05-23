import { Injectable } from '@angular/core';
import { AuthConfig, OAuthService } from 'angular-oauth2-oidc';
import { BehaviorSubject } from 'rxjs';
import { ConfigService } from '../services/config.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  username = new BehaviorSubject<string>('');
  roles = new BehaviorSubject<string[]>([]);
  oidcLoaded = new BehaviorSubject<boolean>(false);

  constructor(private oAuthService: OAuthService, private configService: ConfigService) {}

  logOut(): void {
    this.oAuthService.logOut();
    this.username.unsubscribe();
    this.roles.unsubscribe();
  }

  async initializeLogin(): Promise<void> {
    this.oAuthService.configure({
      issuer: this.configService.config.keycloakUrl,
      redirectUri: window.location.origin,
      clientId: 'leocode-spa',
      responseType: 'code',
      scope: 'offline_access',
      showDebugInformation: true,
      requireHttps: false,
      logoutUrl: window.location.origin
    });
    await this.oAuthService.loadDiscoveryDocumentAndTryLogin({
      customHashFragment: window.location.search
    });

    if (!this.oAuthService.hasValidAccessToken()) {
      this.oAuthService.initLoginFlow();
    } else {
      this.oAuthService.setupAutomaticSilentRefresh();
      const profile = await this.oAuthService.loadUserProfile();
      this.username.next(profile.preferred_username);
      this.roles.next(this.parseJwt(this.oAuthService.getAccessToken()).realm_access.roles);
      this.oidcLoaded.next(true);
    }
  }

  // https://stackoverflow.com/a/38552302/11125147
  private parseJwt(token): any {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );

    return JSON.parse(jsonPayload);
  }
}
