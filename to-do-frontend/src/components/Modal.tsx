import { ReactNode } from 'react';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  children: ReactNode;
}

const Modal: React.FC<ModalProps> = ({ isOpen, onClose, title, children }) => {
  if (!isOpen) return null;

  return (
    
    <div 
      className="fixed inset-0 bg-black bg-opacity-50 z-40 flex justify-center items-center"
      onClick={onClose} 
    >
      
      <div 
        className="bg-white rounded-lg shadow-xl p-6 w-full max-w-lg relative z-50 mx-4"
        onClick={(e) => e.stopPropagation()} 
      >
        
        <div className="flex justify-between items-center border-b pb-3 mb-4">
          <h2 className="text-2xl font-semibold text-slate-800">{title}</h2>
          <button 
            onClick={onClose}
            className="text-slate-500 hover:text-slate-800 text-2xl"
          >
            &times;
          </button>
        </div>
        
        <div>
          {children}
        </div>
      </div>
    </div>
  );
};

export default Modal;