import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { map, take, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

interface UserResponse {
  username: string;
  id: number;
  isDeleted: boolean;
  isAdmin: boolean;
  message?: string;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  users = new BehaviorSubject<UserResponse[]>(null);

  constructor(private http: HttpClient) {}

  fetchUsers() {
    return this.http
      .get<UserResponse[]>(environment.apiUrl + '/admin/list')
      .pipe(
        map((users) => {
          return users.map((user) => {
            return {
              username: user.username,
              id: user.id,
              isAdmin: user.isAdmin,
              isDeleted: user.isDeleted,
            };
          });
        }),
        tap((users) => {
          this.users.next(users);
        }),
        take(1)
      )
      .subscribe();
  }

  deleteUser(userid) {
    return this.http
      .delete<Number>(environment.apiUrl + `/admin/delete/${userid}`)
      .pipe(take(1))
      .subscribe((response) => {
        this.fetchUsers();
      });
  }
}
