export interface IRoom {
  id: number;
  name?: string | null;
  code?: string | null;
}

export type NewRoom = Omit<IRoom, 'id'> & { id: null };
