import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css'],
})
export class AdminComponent implements OnInit {
  users = [
    { username: 'user123', userid: '123asd123' },
    { username: 'this is hardcoded', userid: 'youneedtochangeitlater' },
  ];
  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.adminService.fetchUsers();
    this.users = this.adminService.users;
  }

  fetchUsers() {
    this.adminService.fetchUsers();
    this.users = this.adminService.users;
  }

  onDeleteUser(userid) {
    this.adminService.deleteUser(userid);
  }
}
