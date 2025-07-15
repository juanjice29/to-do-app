import axios from 'axios';
import { Task } from '../types'; // Nota la ruta relativa

const apiClient = axios.create({
  // Se accede a las variables con import.meta.env en lugar de process.env
  baseURL: "http://localhost:8080/api/v1", 
  headers: {
    'Content-Type': 'application/json',
  },
});

export const getTasks = () => apiClient.get<Task[]>('/task');
export const createTask = (taskData: { title: string; description: string }) => apiClient.post<Task>('/task', taskData);
export const updateTask = (id: number, taskData: Partial<Task>) => apiClient.patch<Task>(`/task/${id}`, taskData);
export const deleteTask = (id: number) => apiClient.delete(`/task/${id}`);
export const updateTaskOrder = (id: number, orderBefore: number | null, orderAfter: number | null) => 
  apiClient.patch(`/task/${id}/order`, { orderBefore, orderAfter });