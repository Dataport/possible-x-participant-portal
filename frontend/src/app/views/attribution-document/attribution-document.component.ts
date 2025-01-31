import { Component } from '@angular/core';
import {ViewportScroller} from "@angular/common";

@Component({
  templateUrl: './attribution-document.component.html',
  styleUrls: ['./attribution-document.component.scss']
})
export class AttributionDocumentComponent {
  constructor(private readonly viewportScroller: ViewportScroller) {}

  public onClick(elementId: string): void {
    this.viewportScroller.scrollToAnchor(elementId);
  }
}
