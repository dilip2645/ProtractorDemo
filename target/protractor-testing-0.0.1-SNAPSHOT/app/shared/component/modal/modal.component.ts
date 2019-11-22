import { Component, ContentChild, Input, TemplateRef, HostListener, ElementRef, ChangeDetectorRef, Renderer2 } from '@angular/core';

@Component({
  selector: 'sun-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent {
  @Input() headerTitle: string;
  className: string = 'modal-open';
  visible: boolean = false;

  constructor(private renderer: Renderer2, private elementRef: ElementRef, private changeDetectorRef: ChangeDetectorRef) {}

  ngOnDestroy() {
    this.close();
  }

  open() {
    this.renderer.addClass(document.body, this.className);
    this.visible = true;
  }

  close() {
    this.renderer.removeClass(document.body, this.className);
    setTimeout(() => {
      this.visible = false;
      this.changeDetectorRef.markForCheck();
    }, 200);
  }

  @HostListener('document:keydown', ['$event'])
  onKeyDownHandler(event: KeyboardEvent) {
    if (event.key === 'Escape' && this.isTopMost()) {
      this.close();
    }
  }

  isTopMost() {
    return !this.elementRef.nativeElement.querySelector(':scope modal > .modal');
  }
}
