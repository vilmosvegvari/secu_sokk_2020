import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { take } from 'rxjs/operators';
import { environment } from '../../environments/environment';

interface UploadAnswer {
  status: string;
}

@Injectable({ providedIn: 'root' })
export class UploadService {
  constructor(private http: HttpClient) {}

  Upload(file: File) {
    let formData: FormData = new FormData();
    formData.append('file', file, file.name);

    return this.http.put<UploadAnswer>(
      environment.apiUrl + '/upload',
      formData
    );
  }
}
