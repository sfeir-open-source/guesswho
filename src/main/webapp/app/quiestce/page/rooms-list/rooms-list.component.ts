import { Component } from '@angular/core';
import {RoomsService} from "../../service/rooms.service";
import {async} from "rxjs";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {map} from "rxjs/operators";

@Component({
  standalone: true,
  selector: 'rooms-list',
  templateUrl: './rooms-list.component.html',
  imports: [
    AsyncPipe,
    NgForOf,
    RouterLink,
    NgIf
  ],
  styleUrls: ['./rooms-list.component.scss']
})
export class RoomsListComponent {

  public rooms$ = this.roomsService.getMyRooms$().pipe(map(rooms => {
    rooms.sort((room1, room2) => room2.id - room1.id)
    return rooms;
  }));
  public constructor(private roomsService: RoomsService) {
  }
}
