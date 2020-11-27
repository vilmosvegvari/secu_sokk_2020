import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

interface PictureMiniResponse {
  id : number,
  name: string,
  filename: string,
  filesize: number
}

@Injectable({ providedIn: 'root' })
export class PicturesService {
  pictures = new BehaviorSubject<PictureMiniResponse[]>(null);

  constructor(private http: HttpClient) {}

  fetchPictures() {
    console.log('fetching');
    return this.http
      .get<PictureMiniResponse[]>(environment.apiUrl + '/caff/all')
      .pipe(
        map((pictures) => {
          return pictures.map((picture) => {
            return {
              id: picture.id,
              name: picture.name,
              filename: picture.filename,
              filesize: picture.filesize,
            };
          });
        }),
        tap((pictures) => {
          console.log(pictures);
          this.pictures.next(pictures);
        })
      )
      .subscribe();
  }

  deletePicture(pictureId) {
    return this.http
          .delete<Number>(environment.apiUrl + `/caff/delete/${pictureId}`, { observe: 'response' })
          .subscribe((response) => {
            if(response.status === 200) {
              this.pictures.value.splice(this.pictures.value.findIndex((el) => el.id === pictureId),1);
            }
          });
  }

   downloadPicture(pictureId) {
      this.http.get<Blob>(environment.apiUrl + `/download/${pictureId}`)
      .subscribe(response => {
        const blob = new Blob([response], { type: 'caff' });
        const url= window.URL.createObjectURL(blob);
        window.open(url);
      });

   }
}
