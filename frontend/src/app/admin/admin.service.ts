import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

interface UserResponse {
  username: string;
  id: number;
  isDeleted: boolean;
  roles: [{ id: number; name: string }];
  message: string;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  users = [];

  constructor(private http: HttpClient) {}

  fetchUsers() {
    console.log('fetching');
    return this.http
      .get<UserResponse[]>(environment.apiUrl + '/admin/list')
      .pipe(
        map((users) => {
          return users.map((user) => {
            return {
              username: user.username,
              userid: user.id,
              isAdmin: user.roles[0].name === 'ROLE_ADMIN' ? true : false,
              isDeleted: user.isDeleted,
            };
          });
        }),
        tap((users) => {
          this.users = users;
        })
      );
  }

  deleteUser(userid) {
    return this.http
      .delete<UserResponse>(environment.apiUrl + `/admin/delete/${userid}`)
      .subscribe((response) => {
        console.log(response); //now its "OK", we want userid
        let user = this.users.find((user) => user.userid === userid);
        let index = this.users.indexOf(user);
        this.users.splice(index, 1);
      });
  }
}
