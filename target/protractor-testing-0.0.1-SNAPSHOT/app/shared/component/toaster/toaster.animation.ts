import { state, trigger, transition, style, animate, AnimationTriggerMetadata, keyframes } from '@angular/animations';

export const toasterAnimation: AnimationTriggerMetadata = trigger('toasterState', [
  state('void', style({ opacity: 0 })),
  state(
    '*',
    style({
      opacity: 1
    })
  ),
  transition('void <=> *', animate('0.5s ease-in-out'))
]);
