import { Component, OnInit } from '@angular/core';
import { UploadService } from './upload.service';
import { NgForm } from '@angular/forms';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css'],
})
export class UploadComponent implements OnInit {
  file = null;
  fileName = '';
  fileSelected = false;
  responseStatus = '';

  constructor(private uploadService: UploadService) {}

  ngOnInit(): void {
    this.onClickReset();
  }

  fileChange(file) {
    this.fileSelected = file.target.files.length > 0;
    if (this.fileSelected) {
      this.file = file.target.files[0];
      this.fileName = this.file.name;
    } else {
      this.onClickReset();
    }
  }

  onClickUpload(form: NgForm) {
    if (this.fileSelected) {
      this.uploadService
        .Upload(this.file)
        .pipe(take(1))
        .subscribe((response) => {
          this.responseStatus = response.status;
        });
    }

    form.reset();
  }

  onClickReset() {
    this.file = null;
    this.fileName = 'Choose file';
    this.fileSelected = false;
  }
}
