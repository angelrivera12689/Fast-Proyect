import { useState } from 'react';
import AdminSidebar from '../components/Admin/AdminSidebar';
import AdminDashboard from '../components/Admin/AdminDashboard';
import AdminProducts from '../components/Admin/AdminProducts';
import AdminOrders from '../components/Admin/AdminOrders';
import AdminClients from '../components/Admin/AdminClients';
import AdminHero from '../components/Admin/AdminHero';

export default function AdminPage({ onNavigate }) {
  const [section, setSection] = useState('dashboard');

  const renderSection = () => {
    switch (section) {
      case 'products': return <AdminProducts />;
      case 'orders':   return <AdminOrders />;
      case 'clients':  return <AdminClients />;
      case 'hero':     return <AdminHero />;
      default:         return <AdminDashboard />;
    }
  };

  return (
    <div className="min-h-screen bg-[#050d1a] flex">
      <AdminSidebar
        active={section}
        onNavigate={setSection}
        onExit={() => onNavigate('home')}
      />
      <main className="flex-1 overflow-y-auto">
        {/* Top bar */}
        <div className="sticky top-0 z-10 flex items-center justify-between px-8 py-4 bg-[#050d1a]/80 backdrop-blur-md border-b border-teal-500/10">
          <p className="text-teal-400/40 text-xs tracking-widest uppercase capitalize">
            {section === 'hero' ? 'Contenido Landing' : section}
          </p>
          <div className="flex items-center gap-3">
            <div className="w-7 h-7 rounded-full bg-gradient-to-br from-teal-500 to-cyan-600 flex items-center justify-center text-white text-xs font-bold">
              A
            </div>
            <p className="text-teal-100/60 text-sm">Administrador</p>
          </div>
        </div>

        {/* Content */}
        <div className="px-8 py-8">
          {renderSection()}
        </div>
      </main>
    </div>
  );
}