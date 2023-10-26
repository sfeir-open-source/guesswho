import {Component, EventEmitter, Input, Output} from '@angular/core';
import {GameCard} from "../../../../domain/GameCard.model";
import {GameService} from "../../service/game.service";
import {AsyncPipe, NgIf} from "@angular/common";

@Component({
  standalone: true,
  selector: 'game-card',
  templateUrl: './game-card.component.html',
  imports: [
    AsyncPipe,
    NgIf
  ],
  styleUrls: ['./game-card.component.scss']
})
export class GameCardComponent {
  @Input() isSelected = false;
  @Input() isDisabled = false;
  @Input() gameCard: GameCard | undefined = undefined;
  @Output() clickOnCard = new EventEmitter<boolean>();

  cardsPaths$ = this.gameService.cardsPaths$;
  public constructor(private gameService: GameService) {}
}
