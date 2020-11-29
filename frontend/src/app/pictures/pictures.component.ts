import { Component, OnDestroy, OnInit } from '@angular/core';
import { PicturesService } from './pictures.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { environment } from '../../environments/environment';
import { CoreEnvironment } from '@angular/compiler/src/compiler_facade_interface';

@Component({
  selector: 'app-pictures',
  templateUrl: './pictures.component.html',
  styleUrls: ['./pictures.component.css'],
})
export class PicturesComponent implements OnInit, OnDestroy {
  pictures = [];
  pictureSub: Subscription;
  apiUrl: string;

  constructor(
    private picturesService: PicturesService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.apiUrl = environment.apiUrl;
    this.pictureSub = this.picturesService.pictures.subscribe(
      (pictures) => (this.pictures = pictures)
    );
    this.fetchPictures();
  }

  ngOnDestroy(): void {
    this.pictureSub.unsubscribe();
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
