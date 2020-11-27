import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

interface PictureDetailResponse {
  id : number
}

@Injectable({ providedIn: 'root' })
export class PictureService {
  picture = new BehaviorSubject<PictureDetailResponse>(null);

  constructor(private http: HttpClient) {}

  fetchPicture(pictureId) {
    console.log('fetching');
    return this.http
      .get<PictureDetailResponse>(environment.apiUrl + `/details/${pictureId}`)
      .pipe(
        map((picture) => {          
            return {
              id: picture.id
            };
          }
        ),
        tap((picture) => {
          console.log(picture);
          this.picture.next(picture);
        }) 
        )
      .subscribe();
  }
}
