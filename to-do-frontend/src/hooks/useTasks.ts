import { useContext } from 'react';
import { TaskContext } from '../context/TaskContext';
// TYPE: El tipo del contexto se importa para que el hook sepa qué devuelve
import { TaskContextType } from '../context/TaskContext';

export const useTasks = (): TaskContextType => {
  const context = useContext(TaskContext);
  if (!context) {
    throw new Error('useTasks must be used within a TaskProvider');
  }
  return context;
};