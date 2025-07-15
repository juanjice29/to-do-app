import { createContext, useReducer, useEffect, ReactNode, useCallback, useMemo } from 'react';
import { getTasks, createTask as apiCreateTask, deleteTask as apiDeleteTask, updateTask as apiUpdateTask, updateTaskOrder as apiUpdateTaskOrder } from '../service/api';
import { Task, TaskCreateData, TaskUpdateData } from '../types/task';

type FilterType = 'all' | 'completed' | 'pending';

interface TaskState {
  tasks: Task[];
  loading: boolean;
  error: string | null;
  filter: FilterType;
}

type TaskAction =
  | { type: 'SET_LOADING' }
  | { type: 'SET_TASKS'; payload: Task[] }
  | { type: 'ADD_TASK'; payload: Task }
  | { type: 'UPDATE_TASK'; payload: Task }
  | { type: 'DELETE_TASK'; payload: number }
  | { type: 'REORDER_TASKS'; payload: Task[] }
  | { type: 'SET_ERROR'; payload: string }
  | { type: 'SET_FILTER'; payload: FilterType };

export interface TaskContextType extends TaskState {
  dispatch: React.Dispatch<TaskAction>;
  addTask: (taskData: TaskCreateData) => Promise<void>;
  updateTask: (taskId: number, taskData: TaskUpdateData) => Promise<void>;
  deleteTask: (taskId: number) => Promise<void>;
  reorderTasks: (taskId: number, orderBefore: number | null, orderAfter: number | null) => Promise<void>;
  changeFilter: (filter: FilterType) => void;
}

export const TaskContext = createContext<TaskContextType | undefined>(undefined);

const taskReducer = (state: TaskState, action: TaskAction): TaskState => {
  switch (action.type) {
    case 'SET_LOADING':
      return { ...state, loading: true };
    case 'SET_TASKS':
      return { ...state, tasks: action.payload, loading: false, error: null };
    case 'ADD_TASK':
      return { ...state, tasks: [action.payload, ...state.tasks] };
    case 'UPDATE_TASK':
      return {
        ...state,
        tasks: state.tasks.map(task =>
          task.id === action.payload.id ? { ...task, ...action.payload } : task
        ),
      };
    case 'DELETE_TASK':
      return {
        ...state,
        tasks: state.tasks.filter(task => task.id !== action.payload),
      };
    case 'REORDER_TASKS':
      return { ...state, tasks: action.payload };
    case 'SET_FILTER':
      return { ...state, filter: action.payload, loading: true };
    case 'SET_ERROR':
      return { ...state, error: action.payload, loading: false };
    default:
      return state;
  }
};

export const TaskProvider = ({ children }: { children: ReactNode }) => {
  const initialState: TaskState = {
    tasks: [],
    loading: false,
    error: null,
    filter: 'all',
  };

  const [state, dispatch] = useReducer(taskReducer, initialState);

  useEffect(() => {
    const fetchTasks = async () => {
      dispatch({ type: 'SET_LOADING' });
      try {
        const completedParam = state.filter === 'all' ? null : state.filter === 'completed';
        const tasks = await getTasks(completedParam);
        dispatch({ type: 'SET_TASKS', payload: tasks });
      } catch (err) {
        dispatch({ type: 'SET_ERROR', payload: 'Error fetching tasks' });
      }
    };
    fetchTasks();
  }, [state.filter]);

  const addTask = useCallback(async (taskData: TaskCreateData) => {
    try {
      const newTask = await apiCreateTask(taskData);
      dispatch({ type: 'ADD_TASK', payload: newTask });
    } catch (err) {
      dispatch({ type: 'SET_ERROR', payload: 'Error creating task' });
    }
  }, []);

  const updateTask = useCallback(async (taskId: number, taskData: TaskUpdateData) => {
    try {
      const updatedTask = await apiUpdateTask(taskId, taskData);
      dispatch({ type: 'UPDATE_TASK', payload: updatedTask });
    } catch (err) {
      dispatch({ type: 'SET_ERROR', payload: 'Error updating task' });
    }
  }, []);

  const deleteTask = useCallback(async (taskId: number) => {
    try {
      await apiDeleteTask(taskId);
      dispatch({ type: 'DELETE_TASK', payload: taskId });
    } catch (err) {
      dispatch({ type: 'SET_ERROR', payload: 'Error deleting task' });
    }
  }, []);
  
  const reorderTasks = useCallback(async (taskId: number, orderBefore: number | null, orderAfter: number | null) => {
     try {
       await apiUpdateTaskOrder(taskId, { orderBefore, orderAfter });
     } catch(err) {
        dispatch({ type: 'SET_ERROR', payload: 'Error reordering tasks' });
     }
  }, []);

  const changeFilter = useCallback((filter: FilterType) => {
    dispatch({ type: 'SET_FILTER', payload: filter });
  }, []);

  const contextValue = useMemo(() => ({
    ...state,
    dispatch,
    addTask,
    updateTask,
    deleteTask,
    reorderTasks,
    changeFilter,
  }), [state, addTask, updateTask, deleteTask, reorderTasks, changeFilter]);

  return (
    <TaskContext.Provider value={contextValue}>
      {children}
    </TaskContext.Provider>
  );
};