export type ModalState<T> = 
  | { type: 'closed' }
  | { type: 'new' }
  | { type: 'edit', dados: T, id: number }
  | { type: 'delete', id: number };
