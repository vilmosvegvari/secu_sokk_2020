import { ThrowStmt } from '@angular/compiler';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/auth/auth.service';
import { PictureService } from './picture.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-picture',
  templateUrl: './picture.component.html',
  styleUrls: ['./picture.component.css']
})
export class PictureComponent implements OnInit, OnDestroy {
  id: number;
  private sub: any;
  apiUrl: string;

  picture = null;
  user = null;

  comment: string ="";

  constructor(private pictureService: PictureService, private authService: AuthService, private route: ActivatedRoute,  private router: Router) { }

  ngOnInit(): void {
    this.sub = this.route.params.subscribe(params => {
      this.id = +params['id'];
   });

   this.apiUrl = environment.apiUrl;
   this.pictureService.picture.subscribe((picture) => (this.picture = picture));
   this.authService.user.subscribe(user => this.user = user);
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

  Delete() {
    this.pictureService.Delete(this.id);
    this.router.navigate(['/pictures']);
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}