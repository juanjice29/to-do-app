
export interface Task {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  createdAt: string; 
  updatedAt: string;
  order: number;
}


export interface TaskCreateData {
  title: string;
  description: string;
  completed?: boolean;
}


export interface TaskUpdateData {
  title?: string;
  description?: string;
  completed?: boolean;
}


export interface TaskOrderData {
  orderBefore: number | null;
  orderAfter: number | null;
}