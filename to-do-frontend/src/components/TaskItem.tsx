import { useState } from 'react';
import { useTasks } from '../hooks/useTasks';
import { Task } from '../types/task';
import Modal from './Modal';
import { getTaskById } from '../service/api';
interface TaskItemProps {
  task: Task;
}

const TaskItem: React.FC<TaskItemProps> = ({ task }) => {
  const { updateTask, deleteTask } = useTasks();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [mode, setMode] = useState<'view' | 'edit'>('view');
  const [fullTask, setFullTask] = useState<Task | null>(null);
  const [isLoadingDetails, setIsLoadingDetails] = useState(false);
  
  const [editedTitle, setEditedTitle] = useState(task.title);
  const [editedDescription, setEditedDescription] = useState('');

  const handleToggleComplete = () => {
    updateTask(task.id, { completed: !task.completed });
  };
  
  const handleDelete = () => {
    deleteTask(task.id);
  };

  const openModalWithDetails = async (initialMode: 'view' | 'edit') => {
    setMode(initialMode);
    setIsModalOpen(true);
    setIsLoadingDetails(true);
    try {
      const taskDetails = await getTaskById(task.id);
      setFullTask(taskDetails);
      if (initialMode === 'edit') {
        setEditedTitle(taskDetails.title);
        setEditedDescription(taskDetails.description || '');
      }
    } catch (error) {
      console.error("Failed to fetch task details:", error);
      setIsModalOpen(false); // Cierra el modal si hay un error
    } finally {
      setIsLoadingDetails(false);
    }
  };

  const handleSaveChanges = () => {
    if (!editedTitle.trim() || !fullTask) return;
    updateTask(fullTask.id, {
      title: editedTitle,
      description: editedDescription,
    });
    setIsModalOpen(false);
  };

  return (
    <>
      <div className="flex items-center justify-between p-4 bg-white shadow-md rounded-lg mb-2 hover:shadow-lg transition-shadow">
        <div className="flex items-center flex-grow cursor-pointer min-w-0" onClick={() => openModalWithDetails('view')}>
          <input 
            type="checkbox" 
            checked={task.completed} 
            onChange={handleToggleComplete}
            onClick={(e) => e.stopPropagation()}
            className="h-5 w-5 rounded border-gray-300 text-blue-600 focus:ring-blue-500 flex-shrink-0"
          />
          <span className={`ml-4 text-lg truncate ${task.completed ? 'line-through text-slate-400' : 'text-slate-800'}`}>
            {task.title}
          </span>
        </div>
        <div className="flex items-center flex-shrink-0 pl-4">
          <button onClick={() => openModalWithDetails('edit')} className="mr-2 p-1 rounded-full hover:bg-slate-200">
            ✏️
          </button>
          <button onClick={handleDelete} className="p-1 rounded-full hover:bg-slate-200">
            🗑️
          </button>
        </div>
      </div>

      <Modal 
        isOpen={isModalOpen} 
        onClose={() => setIsModalOpen(false)} 
        title={isLoadingDetails ? 'Loading...' : (mode === 'edit' ? 'Edit Task' : 'Task Details')}
      >
        {isLoadingDetails ? (
          <p>Loading details...</p>
        ) : mode === 'edit' ? (
          <div>
            <div className="mb-4">
              <label className="block text-sm font-medium text-slate-700 mb-1">Title</label>
              <input 
                type="text" 
                value={editedTitle} 
                onChange={(e) => setEditedTitle(e.target.value)}
                className="w-full p-2 border border-slate-300 rounded-md"
              />
            </div>
            <div className="mb-6">
              <label className="block text-sm font-medium text-slate-700 mb-1">Description</label>
              <textarea 
                value={editedDescription} 
                onChange={(e) => setEditedDescription(e.target.value)}
                rows={4}
                className="w-full p-2 border border-slate-300 rounded-md"
              />
            </div>
            <div className="flex justify-end gap-3">
              <button onClick={() => setIsModalOpen(false)} className="py-2 px-4 rounded-md text-slate-600 bg-slate-100 hover:bg-slate-200">
                Cancel
              </button>
              <button onClick={handleSaveChanges} className="py-2 px-4 rounded-md text-white bg-blue-600 hover:bg-blue-700">
                Save Changes
              </button>
            </div>
          </div>
        ) : (
          fullTask && (
            <div>
              <p className="text-slate-600 mb-4 whitespace-pre-wrap">{fullTask.description || "No description provided."}</p>
              <div className="text-sm text-slate-500 border-t pt-4">
                <p><strong>Created:</strong> {new Date(fullTask.createdAt).toLocaleString()}</p>
                <p><strong>Last Updated:</strong> {new Date(fullTask.updatedAt).toLocaleString()}</p>
              </div>
            </div>
          )
        )}
      </Modal>
    </>
  );
};

export default TaskItem;