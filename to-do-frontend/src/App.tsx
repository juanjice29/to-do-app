import TaskForm from './components/TaskForm';
import TaskList from './components/TaskList';
import './App.css'
import FilterControls from './components/FilterControls';
import { useTasks } from './hooks/useTasks';


function App() {
  // 3. Obtiene el estado del filtro y la función para cambiarlo desde el contexto
  const { filter, changeFilter } = useTasks();

  return (
    <div className="bg-slate-100 min-h-screen flex items-center justify-center font-sans">
      <main className="container mx-auto max-w-2xl p-4 sm:p-6 lg:p-8">
        <header className="text-center my-6">
          <h1 className="text-5xl font-bold text-slate-800 tracking-tight">
            To-Do List
          </h1>
        </header>
        <div className="bg-white p-6 rounded-2xl shadow-xl">
          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4 text-slate-700 border-b pb-2">
              Add New Task
            </h2>
            <TaskForm />
          </section>
          <section>
            <div className="flex justify-between items-center mb-4 border-b pb-2">
              <h2 className="text-2xl font-semibold text-slate-700">
                My Tasks
              </h2>
            </div>
            
            <FilterControls currentFilter={filter} onFilterChange={changeFilter} />
            <TaskList />
          </section>
        </div>
        <footer className="text-center mt-8 text-slate-500 text-sm">
          <p>LG CNS Technical Test - Full Stack Developer - Juan Manuel Jimenez Celis</p>
        </footer>
      </main>
    </div>
  );
}

export default App;