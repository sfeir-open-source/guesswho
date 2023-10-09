import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RoomComponent } from './list/room.component';
import { RoomDetailComponent } from './detail/room-detail.component';
import { RoomUpdateComponent } from './update/room-update.component';
import RoomResolve from './route/room-routing-resolve.service';

const roomRoute: Routes = [
  {
    path: '',
    component: RoomComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoomDetailComponent,
    resolve: {
      room: RoomResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoomUpdateComponent,
    resolve: {
      room: RoomResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoomUpdateComponent,
    resolve: {
      room: RoomResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default roomRoute;
