import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StatusMessageComponent } from './status-message/status-message.component';

@NgModule({
  declarations: [StatusMessageComponent],
  imports: [CommonModule],
  exports: [StatusMessageComponent],
})

export class CommonViewsModule {}