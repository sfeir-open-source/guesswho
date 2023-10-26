import {Component, OnDestroy} from '@angular/core';
import {ThemeService} from "../../service/theme.service";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {GameService} from "../game/service/game.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription, switchMap} from "rxjs";
import {RoomsService} from "../../service/rooms.service";

@Component({
  standalone: true,
  selector: 'jhi-new-game',
  templateUrl: './new-game.component.html',
  imports: [
    AsyncPipe,
    NgForOf,
    NgIf
  ],
  styleUrls: ['./new-game.component.scss']
})
export class NewGameComponent implements OnDestroy {
  themes$ = this.themeService.getThemes$();
  private subscriptions = new Subscription();
  constructor(private themeService: ThemeService, private roomsService: RoomsService,
              private route: ActivatedRoute, private router: Router) {}

  public createGame(themeId: number): void {
    const roomId = parseInt(this.route.snapshot.params["roomId"], 10);
    this.subscriptions.add(this.roomsService.createGame$(roomId, themeId).pipe(switchMap(game => this.router.navigate(["/room", roomId, "game", game.id]))).subscribe());
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
