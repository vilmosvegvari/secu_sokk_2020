import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { BehaviorSubject } from 'rxjs';
import { tap, timeout } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { User } from './user.model';

export interface AuthDataResponse {
  username: string;
  token: string;
  id: string;
  isAdmin: boolean;
  errorMessage?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  user = new BehaviorSubject<User>(null);

  constructor(private http: HttpClient, private router: Router) {}

  signup(username: string, password: string) {
    return this.http
      .post<AuthDataResponse>(environment.apiUrl + '/auth/signup', {
        username: username,
        password: password,
      })
      .pipe(
        timeout(10000),
        tap((resData) => {
          this.handleAuthentication(
            resData.username,
            resData.id,
            resData.token,
            resData.isAdmin
          );
        })
      );
  }

  autoLogin() {
    const userData: {
      username: string;
      id: string;
      _token: string;
      isAdmin: boolean;
    } = JSON.parse(localStorage.getItem('userData'));

    if (!userData) {
      return;
    }

    const loadedUser = new User(
      userData.username,
      userData.id,
      userData._token,
      userData.isAdmin
    );
    this.user.next(loadedUser);
  }

  login(username: string, password: string) {
    return this.http
      .post<AuthDataResponse>(environment.apiUrl + '/auth/login', {
        username: username,
        password: password,
      })
      .pipe(
        timeout(10000),
        tap((resData) => {
          this.handleAuthentication(
            resData.username,
            resData.id,
            resData.token,
            resData.isAdmin
          );
        })
      );
  }

  logout() {
    this.user.next(null);
    localStorage.removeItem('userData');
    this.router.navigate(['/auth']);
  }

  private handleAuthentication(
    username: string,
    id: string,
    token: string,
    isAdmin: boolean
  ) {
    const user = new User(username, id, token, isAdmin);
    this.user.next(user);
    localStorage.setItem('userData', JSON.stringify(user));
  }
}
