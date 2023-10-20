import { Component } from '@angular/core';
import {RoomsService} from "../../service/rooms.service";
import {async} from "rxjs";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {ActivatedRoute, RouterLink} from "@angular/router";

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

  rooms$ = this.roomsService.getMyRooms$();
  constructor(private roomsService: RoomsService) {
  }
}
