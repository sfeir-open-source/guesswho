import { IRoom, NewRoom } from './room.model';

export const sampleWithRequiredData: IRoom = {
  id: 13820,
  name: 'sans que du fait que',
  code: 'infime',
};

export const sampleWithPartialData: IRoom = {
  id: 5946,
  name: 'souple broum à côté de',
  code: 'rêver',
};

export const sampleWithFullData: IRoom = {
  id: 23730,
  name: 'à la faveur de',
  code: 'gestionnaire chut',
};

export const sampleWithNewData: NewRoom = {
  name: 'clac prout',
  code: 'secouriste coin-coin',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
