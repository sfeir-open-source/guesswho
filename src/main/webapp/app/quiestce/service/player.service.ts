import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PlayerService {

  constructor(private http: HttpClient) {}

  public getCurrentPlayerId$(): Observable<number> {
    return this.http.get<string>("/api/player").pipe(map(id => parseInt(id, 10)));
  }
}
