import { Component, OnDestroy, OnInit } from '@angular/core';
import { PicturesService } from './pictures.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { environment } from '../../environments/environment';
import { CoreEnvironment } from '@angular/compiler/src/compiler_facade_interface';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-pictures',
  templateUrl: './pictures.component.html',
  styleUrls: ['./pictures.component.css'],
})
export class PicturesComponent implements OnInit, OnDestroy {
  pictures = [];
  pictureSub: Subscription;
  userSub: Subscription;
  apiUrl: string;
  user = null;

  constructor(
    private picturesService: PicturesService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.apiUrl = environment.apiUrl;
    this.pictureSub = this.picturesService.pictures.subscribe(
      (pictures) => (this.pictures = pictures)
    );
    this.userSub = this.authService.user.subscribe(
      (user) => (this.user = user)
    );
    this.fetchPictures();
  }

  ngOnDestroy(): void {
    this.pictureSub.unsubscribe();
    this.userSub.unsubscribe();
  }

  getTagNames(array) {
    return array.map((tag) => {
      return tag.name;
    });
  }

  fetchPictures(): void {
    this.picturesService.fetchPictures();
  }

  onDeletePicture(pictureId) {
    this.picturesService.deletePicture(pictureId);
  }

  onDownloadPicture(pictureId) {
    this.picturesService.downloadCaff(pictureId);
  }

  onOpenPicture(pictureId) {
    let picture = this.pictures.find((p) => p.id === pictureId);
    if (picture.status === 'BAD_FILE') {
      return;
    }
    this.router.navigate(['/pictures', pictureId]);
  }
}
