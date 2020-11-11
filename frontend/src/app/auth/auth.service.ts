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
  errorMessage: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  user = new BehaviorSubject<User>(null);

  constructor(private http: HttpClient, private router: Router) {}

  signup(username: string, password: string) {
    return this.http
      .post<AuthDataResponse>(environment.apiUrl + '/signup', {
        username: username,
        password: password,
      })
      .pipe(
        timeout(10000),
        tap((resData) => {
          this.handleAuthentication(
            resData.username,
            resData.id,
            resData.token
          );
        })
      );
  }

  autoLogin() {
    const userData: {
      username: string;
      id: string;
      _token: string;
    } = JSON.parse(localStorage.getItem('userData'));

    if (!userData) {
      return;
    }

    const loadedUser = new User(
      userData.username,
      userData.id,
      userData._token
    );
    this.user.next(loadedUser);
  }

  login(email: string, password: string) {
    return this.http
      .post<AuthDataResponse>(environment.apiUrl + '/login', {
        email: email,
        password: password,
      })
      .pipe(
        timeout(10000),
        tap((resData) => {
          this.handleAuthentication(
            resData.username,
            resData.id,
            resData.token
          );
        })
      );
  }

  logout() {
    this.user.next(null);
    localStorage.removeItem('userData');
    this.router.navigate(['/auth']);
  }

  private handleAuthentication(username: string, id: string, token: string) {
    const user = new User(username, id, token);
    this.user.next(user);
    localStorage.setItem('userData', JSON.stringify(user));
  }
}
