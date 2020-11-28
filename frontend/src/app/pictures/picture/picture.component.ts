import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PictureService } from './picture.service';

@Component({
  selector: 'app-picture',
  templateUrl: './picture.component.html',
  styleUrls: ['./picture.component.css']
})
export class PictureComponent implements OnInit, OnDestroy {
  id: number;
  private sub: any;

  picture = null;

  comment: string ="";

  constructor(private pictureService: PictureService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.sub = this.route.params.subscribe(params => {
      this.id = +params['id'];
   });

   this.pictureService.picture.subscribe((picture) => (this.picture = picture));
   this.pictureService.fetchPicture(this.id);
  }

  onComment()
  {
    this.comment = this.comment.trim();
    if(this.comment) {
      this.pictureService.Comment(this.id, this.comment);
      this.comment = '';
    }
  }

  onRemoveComment(commentId) {
    this.pictureService.RemoveComment(this.id, commentId);
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}