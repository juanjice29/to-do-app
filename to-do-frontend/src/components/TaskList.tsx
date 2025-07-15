// TYPE: Importamos DropResult para tipar el resultado del drag-and-drop
import { DragDropContext, Droppable, Draggable, DropResult } from '@hello-pangea/dnd';
import { useTasks } from '../hooks/useTasks';
import TaskItem from './TaskItem';
import { Task } from '../types/task';

const TaskList = () => {
  const { tasks, dispatch, reorderTasks, loading, error } = useTasks();

  // TYPE: Tipamos el parámetro 'result'
  const handleOnDragEnd = (result: DropResult) => {
    if (!result.destination) return;

    const items: Task[] = Array.from(tasks);
    const [reorderedItem] = items.splice(result.source.index, 1);
    items.splice(result.destination.index, 0, reorderedItem);

    dispatch({ type: 'REORDER_TASKS', payload: items });

    const destinationIndex = result.destination.index;
    const orderBefore = items[destinationIndex - 1]?.order ?? null;
    const orderAfter = items[destinationIndex + 1]?.order ?? null;

    const taskId = reorderedItem.id;
    reorderTasks(taskId, orderBefore, orderAfter);
  };

  if (loading) return <p>Loading tasks...</p>;
  if (error) return <p>{error}</p>;

  return (
    <DragDropContext onDragEnd={handleOnDragEnd}>
      {/* El resto del JSX es idéntico */}
      <Droppable droppableId="tasks">
        {(provided) => (
          <ul {...provided.droppableProps} ref={provided.innerRef}>
            {tasks.map((task, index) => (
              <Draggable key={task.id} draggableId={String(task.id)} index={index}>
                {(provided) => (
                  <li ref={provided.innerRef} {...provided.draggableProps} {...provided.dragHandleProps}>
                    <TaskItem task={task} />
                  </li>
                )}
              </Draggable>
            ))}
            {provided.placeholder}
          </ul>
        )}
      </Droppable>
    </DragDropContext>
  );
};

export default TaskList;