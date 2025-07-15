type FilterType = 'all' | 'completed' | 'pending';

interface FilterControlsProps {
  currentFilter: FilterType;
  onFilterChange: (filter: FilterType) => void;
}

const FilterControls: React.FC<FilterControlsProps> = ({ currentFilter, onFilterChange }) => {
  const filters: { label: string; value: FilterType }[] = [
    { label: 'All', value: 'all' },
    { label: 'Pending', value: 'pending' },
    { label: 'Completed', value: 'completed' },
  ];

  return (
    <div className="flex justify-center gap-3 mb-6">
      {filters.map(({ label, value }) => {
        
        const isActive = currentFilter === value;
        const buttonClasses = `py-2 px-4 rounded-md text-sm font-medium transition-colors ${
          isActive
            ? 'bg-blue-600 text-white'
            : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
        }`;

        return (
          <button
            key={value}
            onClick={() => onFilterChange(value)}
            className={buttonClasses}
          >
            {label}
          </button>
        );
      })}
    </div>
  );
};

export default FilterControls;