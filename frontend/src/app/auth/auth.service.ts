import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { tap, timeout } from 'rxjs/operators';
import * as bcrypt from 'bcryptjs';

import { environment } from 'src/environments/environment';
import { User } from './user.model';

export interface AuthDataResponse {
  username: string;
  token: string;
  id: string;
  isAdmin: boolean;
  expirationDate: Date;
  errorMessage?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  user = new BehaviorSubject<User>(null);
  private salt;
  private tokenExpirationTimer: any;

  constructor(private http: HttpClient, private router: Router) {}

  signup(username: string, password: string) {
    if (!this.salt) {
      this.salt = bcrypt.genSaltSync(10);
    }
    var hash = bcrypt.hashSync(password, this.salt);
    return this.http
      .post<AuthDataResponse>(environment.apiUrl + '/auth/signup', {
        username: username,
        password: hash,
      })
      .pipe(
        timeout(10000),
        tap((resData) => {
          this.handleAuthentication(
            resData.username,
            resData.id,
            resData.token,
            resData.isAdmin,
            resData.expirationDate
          );
        })
      );
  }

  autoLogin() {
    if (!this.salt) {
      this.salt = bcrypt.genSaltSync(10);
    }
    const userData: {
      username: string;
      id: string;
      _token: string;
      isAdmin: boolean;
      expirationDate: Date;
    } = JSON.parse(localStorage.getItem('userData'));

    if (!userData) {
      return;
    }

    const loadedUser = new User(
      userData.username,
      userData.id,
      userData._token,
      userData.isAdmin,
      userData.expirationDate
    );
    this.user.next(loadedUser);
    if (userData.expirationDate) {
      let expires = new Date(userData.expirationDate);
      let difference = Math.abs(new Date().getTime() - expires.getTime());
      this.autoLogout(difference);
    }
  }

  login(username: string, password: string) {
    if (!this.salt) {
      this.salt = bcrypt.genSaltSync(10);
    }
    var hash = bcrypt.hashSync(password, this.salt);
    return this.http
      .post<AuthDataResponse>(environment.apiUrl + '/auth/login', {
        username: username,
        password: hash,
      })
      .pipe(
        timeout(10000),
        tap((resData) => {
          this.handleAuthentication(
            resData.username,
            resData.id,
            resData.token,
            resData.isAdmin,
            resData.expirationDate
          );
        })
      );
  }

  logout() {
    this.user.next(null);
    localStorage.removeItem('userData');
    this.router.navigate(['/auth']);
  }

  autoLogout(expirationDuration: number) {
    this.tokenExpirationTimer = setTimeout(() => {
      this.logout();
    }, expirationDuration);
  }

  private handleAuthentication(
    username: string,
    id: string,
    token: string,
    isAdmin: boolean,
    expirationDate: Date
  ) {
    const user = new User(username, id, token, isAdmin, expirationDate);
    this.user.next(user);
    localStorage.setItem('userData', JSON.stringify(user));
    let expires = new Date(expirationDate);
    let difference = Math.abs(new Date().getTime() - expires.getTime());
    this.autoLogout(difference);
  }
}
