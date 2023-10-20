import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Room} from "../domain/Room.model";
import {Observable, switchMap} from "rxjs";
import {filter, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class RoomsService {

  constructor(private http: HttpClient) {}

  public getMyRooms$(): Observable<Room[]>  {
    return this.http.get<Room[]>("/api/rooms"); // TODO "/api"
  }

  public getRoom$(id: number): Observable<Room>  {
    //return this.http.get<Room>(`/api/room/${id}`);
    return this.getMyRooms$().pipe(map(rooms => {
      const room = rooms.filter(room => room.id === id)[0];
      if (!room) {
        throw new Error("room not found");
      }
      return room;
    }));
  }
}
