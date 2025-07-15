import axios from 'axios';
import { Task, TaskCreateData, TaskUpdateData, TaskOrderData } from '../types/task';

const API = axios.create({
  baseURL: 'http://localhost:8080/api/v1/task',
});

// TYPE: Añadimos tipos a los parámetros y al valor de retorno (Promise<Task[]>)
export const getTasks = async (completed: boolean | null = null): Promise<Task[]> => {
  const params: { completed?: boolean } = {};
  if (completed !== null) {
    params.completed = completed;
  }
 
  const response = await API.get<Task[]>('/', { params });
  return response.data;
};

export const createTask = async (taskData: TaskCreateData): Promise<Task> => {
  const response = await API.post<Task>('/', taskData);
  return response.data;
};

export const updateTask = async (taskId: number, updateData: TaskUpdateData): Promise<Task> => {
  const response = await API.patch<Task>(`/${taskId}`, updateData);
  return response.data;
};

export const deleteTask = async (taskId: number): Promise<void> => {
  await API.delete(`/${taskId}`);
};

export const updateTaskOrder = async (taskId: number, orderData: TaskOrderData): Promise<void> => {
  await API.patch(`/${taskId}/order`, orderData);
};

export const getTaskById = async (taskId: number): Promise<Task> => {
  const response = await API.get<Task>(`/${taskId}`);
  return response.data;
};