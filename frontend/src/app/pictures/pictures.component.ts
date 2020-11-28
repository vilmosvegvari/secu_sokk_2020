import { Component, OnDestroy, OnInit } from '@angular/core';
import { PicturesService } from './pictures.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-pictures',
  templateUrl: './pictures.component.html',
  styleUrls: ['./pictures.component.css'],
})
export class PicturesComponent implements OnInit, OnDestroy {
  pictures = [];
  pictureSub: Subscription;

  constructor(
    private picturesService: PicturesService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.pictureSub = this.picturesService.pictures.subscribe(
      (pictures) => (this.pictures = pictures)
    );
    this.fetchPictures();
  }

  ngOnDestroy(): void {
    this.pictureSub.unsubscribe();
  }

  fetchPictures(): void {
    this.picturesService.fetchPictures();
  }

  onDeletePicture(pictureId) {
    this.picturesService.deletePicture(pictureId);
  }

  onDownloadPicture(pictureId) {
    this.picturesService.downloadCaff(pictureId);
    this.picturesService.downloadPNG(pictureId);
    this.picturesService.downloadGIF(pictureId);
  }

  onOpenPicture(pictureId) {
    this.router.navigate(['/pictures', pictureId]);
  }
}
