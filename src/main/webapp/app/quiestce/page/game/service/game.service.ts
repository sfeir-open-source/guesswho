import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {
  combineLatest,
  distinctUntilChanged,
  Observable,
  of,
  OperatorFunction,
  ReplaySubject,
  Subscription,
  switchMap
} from "rxjs";
import {Game} from "../../../domain/Game.model";
import {catchError, filter, map, shareReplay, tap} from "rxjs/operators";
import {ThemeService} from "../../../service/theme.service";
import {ThemeCard} from "../../../domain/ThemeCard.model";
import {PlayerService} from "../../../service/player.service";
import {RoomsService} from "../../../service/rooms.service";
import {Room} from "../../../domain/Room.model";
import {GameCard} from "../../../domain/GameCard.model";
import {Message} from "../../../domain/Message.model";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class GameService {
  public messages$ = new ReplaySubject<Message[]>(1);

  private gameId: number | undefined;
  private roomId: number | undefined;
  private playerId: number | undefined;
  private roomData: Room | undefined;
  private _gameData$ = new ReplaySubject<Game>(1);
  private getGameDataSubscription = new Subscription();
  private currentThemeId: number | undefined = undefined;
  private themeCards: ThemeCard[] = [];
  private lastMessageId = 0;
  private messages: Message[] = [];

  constructor(private http: HttpClient, private themeService: ThemeService, private playerService: PlayerService,
              private roomsService: RoomsService, private router: Router) {}

  public setGame(roomId: number, gameId: number, playerId: number): void {
    this.roomId = roomId;
    this.roomData = undefined;
    this.gameId = gameId;
    this.playerId = playerId;
    this.messages = [];
    this.messages$.next([]);
    this.lastMessageId = 0;
    this.updateDataFromServer();
  }

  public updateDataFromServer(): void {
    if (this.gameId === undefined) {
      throw new Error("game data cannot be updated - no game set");
    }
    this.getGameDataSubscription.unsubscribe();
    const roomObs = this.roomData ? of(this.roomData) : this.roomsService.getRoom$(this.roomId!);
    this.getGameDataSubscription = combineLatest([roomObs, this.getGameData$(), this.getLastMessages$()]).pipe(
      tap(([roomData, gameData, lastMessages]) => {
        this.roomData = roomData;
        lastMessages.forEach(message => this.messages.push(message));
        if (lastMessages.length>0) {
          this.messages$.next(this.messages);
          this.lastMessageId = lastMessages[lastMessages.length-1].id;
        }
        this._gameData$.next(gameData);
    })).subscribe();
  }

  public selectMyCard$(gameCardId: number): Observable<boolean> {
    return this.http.post<Game>(`/api/games/${this.gameId}/select-card`, { gameCardId }).pipe(map(() => {
      this.updateDataFromServer();
      return true;
    }));
  }

  public play$(gameCardIds: number[]): Observable<boolean> {
    return this.http.post<Game>(`/api/games/${this.gameId}/play`, { gameCardIds }).pipe(map(() => {
      this.updateDataFromServer();
      return true;
    }));
  }

  public sendMessage$(content: string): Observable<boolean> {
    return this.http.post<Game>(`/api/games/${this.gameId}/message`, { content }).pipe(map(() => {
      this.updateDataFromServer();
      return true;
    }));
  }


  get gameData$(): Observable<Game> {
    return this._gameData$;
  }

  get gameCards$(): Observable<GameCard[]> {
    return this.gameData$.pipe(
      map(game => game.gameCards),
      distinctUntilChanged((prev, cur) => JSON.stringify(prev)===JSON.stringify(cur))
    );
  }

  get themeCards$(): Observable<ThemeCard[]> {
    return this._gameData$.pipe(
      switchMap((gameData) => {
        if (gameData.theme.id === this.currentThemeId) {return of(this.themeCards);}
        return combineLatest([of(gameData), this.themeService.getThemeCards$(gameData.theme.id)]).pipe(map(([gameData2, themeCards]) => {
          this.themeCards = themeCards;
          this.currentThemeId = gameData2.theme.id;
          return themeCards;
        }))
      }));
  }

  get shouldSelectFirstCard$(): Observable<boolean> {
    return this._gameData$.pipe(map(gameData => gameData.gameCards.filter(card => {
        if (this.roomData?.player1.id===this.playerId) {
          return card.player1_chosen;
        } else if (this.roomData?.player2.id===this.playerId) {
          return card.player2_chosen;
        }
        throw new Error(`player ${this.playerId} not found in room`)
      }).length===0));
  }

  get hasGameStarted$(): Observable<boolean> {
    return this._gameData$.pipe(map(gameData => !!gameData.nextTurn));
  }

  get disabledCardsIds$(): Observable<number[]> {
    return this.gameData$.pipe(map(gameData => gameData.gameCards.filter(card => {
        if (this.roomData?.player1.id===this.playerId) {
          return card.player1_discarded;
        } else if (this.roomData?.player2.id===this.playerId) {
          return card.player2_discarded;
        }
        throw new Error(`player ${this.playerId} not found in room`)
      }).map(card => card.id)));
  }

  get isWaitingForOtherPlayerToChooseCard$(): Observable<boolean> {
    return combineLatest([this.shouldSelectFirstCard$, this._gameData$]).pipe(map(([shouldSelectFirstCard, gameData]) => !shouldSelectFirstCard && !gameData.nextTurn));
  }

  get hasGameEnded$(): Observable<boolean> {
    return this._gameData$.pipe(map(gameData => !!gameData.winner));
  }

  get hasWon$(): Observable<boolean> {
    return this._gameData$.pipe(map(gameData => gameData.winner?.id === this.playerId));
  }

  get otherPlayerCard$(): Observable<GameCard> {
    return this._gameData$.pipe(map(gameData => {
      const cards = gameData.gameCards.filter(card => {
        if (this.roomData?.player1.id===this.playerId) {
          return card.player2_chosen;
        } else if (this.roomData?.player2.id===this.playerId) {
          return card.player1_chosen;
        }
        throw new Error(`player ${this.playerId} not found in room`)
      });
      return cards.length>0 ? cards[0] : undefined;
    }), filter(card => !!card) as OperatorFunction<GameCard | undefined, GameCard>);
  }

  get isMyTurn$(): Observable<boolean> {
    return this._gameData$.pipe(map(gameData => gameData.nextTurn?.id === this.playerId));
  }

  get cardsPaths$(): Observable<Record<number, Observable<string>>> {
    return this.gameCards$.pipe(map(gameCards => {
      const pathsMap: Record<number, Observable<string>> = {};
      gameCards.forEach(gameCard => {
        pathsMap[gameCard.id] = this.themeCards$.pipe(map(themeCards => {
          const themeCard = themeCards.find(tc => tc.id === gameCard.themeCard.id);
          if (!themeCard) {
            throw new Error(`theme card (${gameCard.themeCard.id}) not found for game card ${gameCard.id}`);
          }
          return themeCard.picture.path;
        }));
      });
      return pathsMap;
    }), shareReplay(1));
  }

  private getGameData$(): Observable<Game> {
    return this.http.get<Game>(`/api/games/${this.gameId}`).pipe(catchError(err => {
      console.error("game not found");
      this.router.navigate(['/']).catch(() => {});
      throw err;
    }));
  }

  private getLastMessages$(): Observable<Message[]> {
    return this.http.get<Message[]>(`/api/games/${this.gameId}/message?lastMessageId=${this.lastMessageId}`);
  }
}
