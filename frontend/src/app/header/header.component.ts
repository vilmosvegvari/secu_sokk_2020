import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit, OnDestroy {
  isAuthenticated = false;
  isAdmin = false;
  private userSub: Subscription;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.userSub = this.authService.user.subscribe((user) => {
      this.isAuthenticated = !!user;
      if (user) {
        this.isAdmin = user.isAdmin;
      }
    });
  }
  ngOnDestroy(): void {
    this.userSub.unsubscribe();
  }

  inPictures() {
    return this.router.url === '/pictures';
  }

  onLogout() {
    if (!this.isAuthenticated) {
      return;
    }
    this.authService.logout();
  }
}
