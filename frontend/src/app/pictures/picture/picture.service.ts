import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

interface Comment {
  id: number
  date : Date,
  message: string,
  userName: string
}


interface PictureDetailResponse {
  id : number,
  name: string,
  userName: string,
  comments: Comment[],
  tags: string[]
}

@Injectable({ providedIn: 'root' })
export class PictureService {
  picture = new BehaviorSubject<PictureDetailResponse>(null);
  gifUrl = new BehaviorSubject<string>(null);

  constructor(private http: HttpClient) {}

  fetchPicture(pictureId) {
    console.log('fetching');

    return this.http
      .get<PictureDetailResponse>(environment.apiUrl + `/caff/details/${pictureId}`)
      .pipe(
        map((picture) => {          
            return {
              id: picture.id,
              name: picture.name,
              userName: picture.userName,
              comments: picture.comments,
              tags: picture.tags
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

  Comment(pictureId: number, comment : string) {

    let formData:FormData = new FormData();
        formData.append('message', comment);

    this.http.post(environment.apiUrl + `/caff/details/${pictureId}/comment`, formData)
    .subscribe((response) => {
        console.log(response);
        this.fetchPicture(pictureId);
      });
  }

  RemoveComment(pictureId: number, commentId : number) {
    this.http.delete(environment.apiUrl + `/caff/details/${pictureId}/comment/${commentId}`)
    .subscribe((response) => {
        console.log(response);
        this.fetchPicture(pictureId);
      });
  }

  Delete(pictureId: Number) {
    return this.http
          .delete<Number>(environment.apiUrl + `/caff/delete/${pictureId}`, { observe: 'response' })
          .subscribe();
  }
}
