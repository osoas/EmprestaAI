import React, { useEffect, useState } from 'react';
import { Icons } from './Icons';
import { AppLogo } from './AppLogo';

export const HeaderWave: React.FC<{ title: string; subtitle?: string; showBack?: boolean; onBack?: () => void; className?: string; showLogo?: boolean }> = ({ title, subtitle, showBack, onBack, className, showLogo }) => {
  return (
    <div className="relative bg-black text-white flex flex-col animate-fade-in">
      <div className="w-full leading-[0] overflow-hidden bg-black order-2">
        <div className="relative w-[200%] flex animate-wave">
          <svg className={`block w-1/2 h-12 md:h-16 ${className || 'text-white'} fill-current rotate-180`} preserveAspectRatio="none" viewBox="0 0 1440 320" xmlns="http://www.w3.org/2000/svg">
            <path d="M0,160 C320,300, 420,300, 720,160 C1000,20, 1100,20, 1440,160 V320 H0 Z"></path>
          </svg>
          <svg className={`block w-1/2 h-12 md:h-16 -ml-[1px] ${className || 'text-white'} fill-current rotate-180`} preserveAspectRatio="none" viewBox="0 0 1440 320" xmlns="http://www.w3.org/2000/svg">
            <path d="M0,160 C320,300, 420,300, 720,160 C1000,20, 1100,20, 1440,160 V320 H0 Z"></path>
          </svg>
        </div>
      </div>




      <div className="absolute z-10 flex flex-col items-center text-center px-6 pt-12 pb-12 order-1  top-[-1020px] ">
        {showBack && (
          <button onClick={onBack} className="absolute left-4 top-12 p-2 rounded-full hover:bg-white/10 transition-colors z-20 hover:scale-110 active:scale-95 transition-transform">
            <Icons.Back className="w-6 h-6 text-black" />
          </button>
        )}

      </div>
    </div>
  );
};

export const Input: React.FC<React.InputHTMLAttributes<HTMLInputElement> & { label?: string }> = ({ label, className, ...props }) => (
  <div className="mb-4 w-full">
    {label && <label className="block text-sm font-medium text-gray-700 mb-1 ml-1">{label}</label>}
    <input
      className={`w-full bg-gray-200 text-gray-900 rounded-2xl px-5 py-3 outline-none focus:ring-2 focus:ring-black transition-all focus:scale-[1.01] ${className}`}
      {...props}
    />
  </div>
);

export const Button: React.FC<React.ButtonHTMLAttributes<HTMLButtonElement> & { variant?: 'primary' | 'secondary', loading?: boolean }> = ({ children, className, variant = 'primary', loading = false, ...props }) => {
  const baseStyles = "w-full py-3.5 rounded-full font-bold transition-transform active:scale-95 flex items-center justify-center gap-2 hover:shadow-xl hover:-translate-y-0.5";
  const variants = {
    primary: "bg-black text-white hover:bg-gray-900 shadow-lg",
    secondary: "bg-gray-200 text-black hover:bg-gray-300"
  };

  return (
    <button className={`${baseStyles} ${variants[variant]} ${className}`} {...props} disabled={props.disabled || loading}>
      {loading && <Spinner />}
      {children}
    </button>
  );
};

export const Spinner: React.FC<{ size?: number }> = ({ size = 16 }) => (
  <svg className="animate-spin text-white" width={size} height={size} viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"></path>
  </svg>
);

export const ItemCard: React.FC<{
  item: any;
  onClick: () => void
}> = ({ item, onClick }) => (
  <div onClick={onClick} className="bg-white rounded-xl overflow-hidden shadow-sm border border-gray-100 cursor-pointer active:scale-95 transition-transform hover:shadow-xl hover:scale-[1.03] duration-300">
    <div className="aspect-square bg-gray-100 relative">
      <img src={item.image} alt={item.name} className="w-full h-full object-cover" />
      <div className="absolute top-2 right-2 bg-white/90 backdrop-blur-sm px-2 py-1 rounded-lg text-[10px] font-bold flex items-center gap-1 shadow-sm">
        <Icons.Star className="w-3 h-3 text-yellow-400 fill-yellow-400" />
        <span>
          {item.ratingAvg !== undefined && item.ratingAvg !== null ? item.ratingAvg.toFixed(1) : (item.rating !== undefined ? item.rating : '—')}
        </span>
        {item.ratingCount ? <span className="text-[10px] text-gray-500 ml-1">({item.ratingCount})</span> : null}
      </div>
    </div>
    <div className="p-3">
      <div className="flex justify-between items-start mb-1">
        <h3 className="font-bold text-sm truncate flex-1">{item.name}</h3>
      </div>
      <div className="flex justify-between items-center">
        <span className="text-xs text-gray-500 bg-gray-100 px-2 py-0.5 rounded-md">{item.type}</span>
        <span className="text-sm font-bold text-black">R$ {Number(item.price).toFixed(2)}</span>
      </div>
    </div>
  </div>
);

export const BottomNav: React.FC<{ active: string; onNavigate: (screen: any) => void }> = ({ active, onNavigate }) => {
  const items = [
    { id: 'home', icon: Icons.Home, label: 'Itens' },
    { id: 'add', icon: Icons.Plus, label: 'Add', highlight: true },
    { id: 'profile', icon: Icons.User, label: 'Perfil' },
  ];

  return (
    <div className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 pb-safe pt-2 px-6 flex justify-around items-end h-20 max-w-md mx-auto z-50">
      {items.map((item) => (
        <button
          key={item.id}
          onClick={() => onNavigate(item.id)}
          className={`flex flex-col items-center gap-1 pb-4 transition-transform active:scale-90 ${item.id === active ? 'text-black' : 'text-gray-400'
            }`}
        >
          {item.highlight ? (
            <div className="w-12 h-12 bg-black text-white rounded-full flex items-center justify-center -mt-8 shadow-lg border-4 border-white">
              <item.icon size={24} />
            </div>
          ) : (
            <item.icon size={24} strokeWidth={item.id === active ? 2.5 : 2} />
          )}
          <span className="text-[10px] font-medium">{item.label}</span>
        </button>
      ))}
    </div>
  );
};

export type ToastType = 'info' | 'success' | 'error';
export interface ToastMessage { id: number; message: string; type?: ToastType }

export const ToastContainer: React.FC = () => {
  const [toasts, setToasts] = useState<ToastMessage[]>([]);

  useEffect(() => {
    const handler = (ev: any) => {
      const detail = ev.detail || ev;
      const msg = detail.message || String(detail);
      const type = detail.type || 'info';
      const id = Date.now() + Math.floor(Math.random() * 1000);
      setToasts((t) => [...t, { id, message: msg, type }]);
    };

    window.addEventListener('showToast', handler as EventListener);
    return () => window.removeEventListener('showToast', handler as EventListener);
  }, []);

  useEffect(() => {
    if (toasts.length === 0) return;
    const timers = toasts.map((toast) => {
      const t = setTimeout(() => {
        setToasts((current) => current.filter((x) => x.id !== toast.id));
      }, 3500);
      return t;
    });
    return () => timers.forEach(clearTimeout);
  }, [toasts]);

  if (toasts.length === 0) return null;

  return (
    <div className="fixed top-6 left-0 right-0 flex flex-col items-center gap-2 z-50 pointer-events-none">
      {toasts.map((toast) => (
        <div key={toast.id} className={`pointer-events-auto max-w-md w-full mx-4 px-4 py-3 rounded-lg shadow-lg text-sm font-medium ${toast.type === 'success' ? 'bg-emerald-500 text-white' : toast.type === 'error' ? 'bg-red-500 text-white' : 'bg-gray-800 text-white'}`}>
          {toast.message}
        </div>
      ))}
    </div>
  );
};

export const showToast = (message: string, type: ToastType = 'info') => {
  try {
    window.dispatchEvent(new CustomEvent('showToast', { detail: { message, type } }));
  } catch (e) {
    const ev = document.createEvent('CustomEvent');
    ev.initCustomEvent('showToast', true, true, { message, type });
    window.dispatchEvent(ev);
  }
};
