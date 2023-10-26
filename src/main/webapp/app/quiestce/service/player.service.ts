import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PlayerService {

  constructor(private http: HttpClient) {}

  public getCurrentPlayerId$(): Observable<number> {
    return this.http.get<string>("/api/player").pipe(map(id => parseInt(id, 10)));
  }

  public isLoggedIn$(): Observable<boolean> {
    return this.getCurrentPlayerId$().pipe(map(() => true), catchError(() => of(false)));
  }
}
