import { Component } from '@angular/core';

@Component({
  selector: 'app-loading-spinner',
  styleUrls: ['./loading-spinner.component.css'],
  template:
    '<div class="lds-spinner"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>',
})
export class LoadingSpinnerComponent {}
