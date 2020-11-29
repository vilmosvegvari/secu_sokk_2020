import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { map, take, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

interface PictureMiniResponse {
  id: number;
  name: string;
  filename: string;
  filesize: number;
  thumbnailUrl: string;
  comments: Comment[];
  tags: Tag[];
  captions: [];
  creator: string;
  gif: string;
  numAnim: number;
}

interface Tag {
  id: number;
  name: string;
  type: string;
}

@Injectable({ providedIn: 'root' })
export class PicturesService {
  //this contains all pictures every time.
  allPictures: PictureMiniResponse[] = [];
  //this is what we show -> we can filter this at searching
  pictures = new BehaviorSubject<PictureMiniResponse[]>(null);

  constructor(private http: HttpClient) {}

  fetchPictures() {
    console.log('fetching');
    return this.http
      .get<PictureMiniResponse[]>(environment.apiUrl + '/caff/all')
      .pipe(
        take(1),
        tap((pictures) => {
          console.log(pictures);
          this.allPictures = pictures;
          this.pictures.next(this.allPictures);
        })
      )
      .subscribe();
  }

  onSearch(text) {
    if (text) {
      let filteredPics = this.allPictures.filter(
        (picture) =>
          picture.name.toUpperCase().includes(text.toUpperCase()) ||
          picture.tags.some((tag) => {
            return tag.name.toUpperCase().includes(text.toUpperCase());
          }) ||
          picture.creator.toUpperCase().includes(text.toUpperCase()) ||
          picture.id.toString().includes(text)
      );

      this.pictures.next(filteredPics);
      return;
    }
    this.pictures.next(this.allPictures);
  }

  deletePicture(pictureId) {
    return this.http
      .delete<Number>(environment.apiUrl + `/caff/delete/${pictureId}`)
      .pipe(take(1))
      .subscribe(() => {
        this.fetchPictures();
      });
  }

  downloadCaff(pictureId) {
    this.http
      .get(environment.apiUrl + `/download/${pictureId}`, {
        responseType: 'blob',
      })
      .pipe(take(1))
      .subscribe((response) => {
        const link = document.createElement('a');
        link.href = URL.createObjectURL(new Blob([response], { type: 'caff' }));
        link.download = 'file.caff';
        link.click();
      });
  }

  downloadPNG(pictureId) {
    this.http
      .get(environment.apiUrl + `/download/thumbnail/${pictureId}`, {
        responseType: 'blob',
      })
      .pipe(take(1))
      .subscribe((response) => {
        console.log(response);
        const link = document.createElement('a');
        link.href = URL.createObjectURL(
          new Blob([response], { type: 'image/png' })
        );
        link.download = 'file.png';
        link.click();
      });
  }

  downloadGIF(pictureId) {
    this.http
      .get(environment.apiUrl + `/download/gif/${pictureId}`, {
        responseType: 'blob',
      })
      .pipe(take(1))
      .subscribe((response) => {
        const link = document.createElement('a');
        link.href = URL.createObjectURL(
          new Blob([response], { type: 'image/gif' })
        );
        link.download = 'file.gif';
        link.click();
      });
  }
}
