import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from "rxjs";
import {GameCard} from "../../../../domain/GameCard.model";
import {CommonModule, NgOptimizedImage} from "@angular/common";
import {map} from "rxjs/operators";
import {ThemeCard} from "../../../../domain/ThemeCard.model";
import {GameService} from "../../service/game.service";

@Component({
  standalone: true,
  selector: 'game-board',
  templateUrl: './game-board.component.html',
  imports: [
    CommonModule,
    NgOptimizedImage
  ],
  styleUrls: ['./game-board.component.scss']
})
export class GameBoardComponent {
  @Input() gameCards$: Observable<GameCard[]> = new Observable<GameCard[]>();
  @Input() themeCards$: Observable<ThemeCard[]> = new Observable<ThemeCard[]>();
  @Input() selectedCardsIds: number[] = [];
  @Input() disabledCardsIds: number[] = [];
  @Output() clickedCardId = new EventEmitter<number>();

  cardsPaths$ = this.gameService.cardsPaths$;

  constructor(private gameService: GameService) {
  }

  protected clickOnCard(gameCardId: number) {
    if (!(gameCardId in this.disabledCardsIds)) {
      this.clickedCardId.emit(gameCardId);
    }
  }
}
