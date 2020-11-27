import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

interface PictureDetailResponse {
  id : number,
  name: string,
  filename: string,
  filesize: number
}

@Injectable({ providedIn: 'root' })
export class PictureService {
  pictures = new BehaviorSubject<PictureDetailResponse>(null);

  constructor(private http: HttpClient) {}

}
