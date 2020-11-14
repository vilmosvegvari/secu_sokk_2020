import { Component, OnInit } from '@angular/core';
import { AdminService } from './admin.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css'],
})
export class AdminComponent implements OnInit {
  users = [];

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.adminService.users.subscribe((users) => (this.users = users));
    this.adminService.fetchUsers();
  }

  onDeleteUser(userid) {
    this.adminService.deleteUser(userid);
  }
}
