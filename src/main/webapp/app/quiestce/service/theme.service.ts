import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Theme} from "../domain/Theme.model";
import {ThemeCard} from "../domain/ThemeCard.model";

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  constructor(private http: HttpClient) {}

  public getThemes$(): Observable<Theme[]> {
    return this.http.get<Theme[]>("/api/themes");
  }

  public getThemeCards$(id: number): Observable<ThemeCard[]> {
    return this.http.get<ThemeCard[]>(`/api/themes/${id}`);
  }
}
