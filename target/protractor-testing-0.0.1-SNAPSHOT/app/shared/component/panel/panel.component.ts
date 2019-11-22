import { Component, ContentChild, Input } from '@angular/core';

@Component({
  selector: 'fhlbny-panel',
  templateUrl: './panel.component.html'
})
export class PanelComponent {
  @Input()
  headerTitle: string;

  @ContentChild('additionalHeaderContent', { static: false }) additionalHeaderContentRef: any;

  constructor() {}
}
