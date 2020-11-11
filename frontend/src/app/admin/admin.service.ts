import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../auth/user.model';
import { map, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

interface UserResponse {
  message: string;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  users = [];

  constructor(private http: HttpClient) {}

  fetchUsers() {
    return this.http.get<User[]>(environment.apiUrl + '/admin/list').pipe(
      tap((users) => {
        this.users = users; //might need map later
      })
    );
  }

  deleteUser(userid) {
    return this.http
      .delete<UserResponse>(environment.apiUrl + `/admin/delete/{${userid}}`)
      .subscribe((response) => {
        console.log(response);
      });
  }
}
