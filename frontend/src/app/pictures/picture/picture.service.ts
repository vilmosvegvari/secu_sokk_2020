import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { map, take, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

interface Comment {
  id: number;
  date: Date;
  message: string;
  userName: string;
}

interface PictureDetailResponse {
  id: number;
  name: string;
  comments: Comment[];
  tags: string[];
  captions: [];
  creator: string;
  filesize: number;
  gif: string;
  numAnim: number;
  thumbnailUrl: string;
}

@Injectable({ providedIn: 'root' })
export class PictureService {
  picture = new BehaviorSubject<PictureDetailResponse>(null);

  constructor(private http: HttpClient) {}

  fetchPicture(pictureId) {
    return this.http
      .get<PictureDetailResponse>(
        environment.apiUrl + `/caff/details/${pictureId}`
      )
      .pipe(take(1))
      .subscribe((picture) => {
        this.picture.next(picture);
      });
  }

  Comment(pictureId: number, comment: string) {
    let formData: FormData = new FormData();
    formData.append('message', comment);

    this.http
      .post(environment.apiUrl + `/caff/details/${pictureId}/comment`, formData)
      .pipe(take(1))
      .subscribe(() => {
        this.fetchPicture(pictureId);
      });
  }

  RemoveComment(pictureId: number, commentId: number) {
    this.http
      .delete(
        environment.apiUrl + `/caff/details/${pictureId}/comment/${commentId}`
      )
      .pipe(take(1))
      .subscribe(() => {
        this.fetchPicture(pictureId);
      });
  }

  Delete(pictureId: Number) {
    return this.http
      .delete<Number>(environment.apiUrl + `/caff/delete/${pictureId}`, {
        observe: 'response',
      })
      .pipe(take(1))
      .subscribe();
  }
}
