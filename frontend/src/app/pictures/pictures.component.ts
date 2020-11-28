import { Component, OnInit } from '@angular/core';
import { PicturesService } from './pictures.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pictures',
  templateUrl: './pictures.component.html',
  styleUrls: ['./pictures.component.css']
})
export class PicturesComponent implements OnInit{
  pictures = [];

  constructor(private picturesService: PicturesService, private router: Router) {}

  ngOnInit(): void {
      this.picturesService.pictures.subscribe((pictures) => (this.pictures = pictures));
      this.fetchPictures();
  }

  fetchPictures(): void {
       this.picturesService.fetchPictures();
  }

  onDeletePicture(pictureId){
      this.picturesService.deletePicture(pictureId);
  }

  onDownloadPicture(pictureId){
      this.picturesService.downloadCaff(pictureId);
      this.picturesService.downloadPNG(pictureId);
      this.picturesService.downloadGIF(pictureId);
  }

  onOpenPicture(pictureId){
    this.router.navigate(['/pictures', pictureId]);
  }
}
