import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Game} from "../domain/Game.model";

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(private http: HttpClient) {}

  getGames$(roomId: number): Observable<Game[]> {
    return this.http.get<Game[]>(`/api/games?roomId=${roomId}`);
  }
}
