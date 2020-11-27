import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';


@Injectable({ providedIn: 'root' })
export class UploadService {

  constructor(private http: HttpClient) {}

  Upload(file : File) {

    let formData:FormData = new FormData();
        formData.append('file', file, file.name);

    this.http.put(environment.apiUrl + '/upload', formData)
    .subscribe((response) => {
        console.log(response);
      });
  }
}
