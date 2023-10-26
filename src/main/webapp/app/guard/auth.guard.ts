import {inject} from "@angular/core";
import {PlayerService} from "../quiestce/service/player.service";
import {tap} from "rxjs/operators";
import {Router} from "@angular/router";
import {Observable} from "rxjs";

export const authGuard = (): Observable<boolean> => {
  const playerService = inject(PlayerService);
  const routerService = inject(Router);
  return playerService.isLoggedIn$().pipe(tap((isLoggedIn) => {
    if (!isLoggedIn) {
      routerService.navigate(['/']).catch(() => {});
    }
  }));
}
