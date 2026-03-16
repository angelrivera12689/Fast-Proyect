import { useState } from 'react';
import { mockOrders } from '../../services/adminData';

const fmt = (n) => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n);

const STATUS_OPTIONS = ['pendiente', 'en_tránsito', 'entregado', 'cancelado'];
const STATUS_STYLES = {
  entregado:   'bg-teal-500/15 text-teal-400 border-teal-500/25',
  en_tránsito: 'bg-amber-500/15 text-amber-400 border-amber-500/25',
  pendiente:   'bg-blue-500/15 text-blue-400 border-blue-500/25',
  cancelado:   'bg-red-500/15 text-red-400 border-red-500/25',
};

export default function AdminOrders() {
  const [orders, setOrders] = useState(mockOrders);
  const [filter, setFilter] = useState('todos');
  const [editingId, setEditingId] = useState(null);

  const filtered = filter === 'todos' ? orders : orders.filter(o => o.status === filter);

  const updateStatus = (id, status) => {
    setOrders(prev => prev.map(o => o.id === id ? { ...o, status } : o));
    setEditingId(null);
  };

  return (
    <div className="flex flex-col gap-6">
      <div>
        <h2 className="font-['Cormorant_Garamond',serif] text-3xl font-bold text-white">Pedidos</h2>
        <p className="text-teal-400/50 text-sm mt-1">{orders.length} pedidos registrados</p>
      </div>

      {/* Filter tabs */}
      <div className="flex gap-2 flex-wrap">
        {['todos', ...STATUS_OPTIONS].map(s => (
          <button key={s} onClick={() => setFilter(s)}
            className={`px-4 py-1.5 rounded-full text-xs font-medium capitalize transition-all border
              ${filter === s
                ? 'bg-teal-500/20 text-teal-300 border-teal-500/40'
                : 'text-teal-100/40 border-teal-500/15 hover:border-teal-500/30 hover:text-teal-200'}`}>
            {s.replace('_', ' ')}
            <span className="ml-1.5 text-teal-500/50">
              {s === 'todos' ? orders.length : orders.filter(o => o.status === s).length}
            </span>
          </button>
        ))}
      </div>

      {/* Table */}
      <div className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-teal-500/10 bg-teal-500/5">
                {['Pedido', 'Cliente', 'Fecha', 'Items', 'Total', 'Estado'].map(h => (
                  <th key={h} className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {filtered.map(order => (
                <tr key={order.id} className="border-b border-teal-500/5 hover:bg-teal-500/3 transition-colors">
                  <td className="py-3.5 px-4 font-mono text-xs text-teal-300">{order.id}</td>
                  <td className="py-3.5 px-4 text-white font-medium">{order.client}</td>
                  <td className="py-3.5 px-4 text-teal-100/40 text-xs">{order.date}</td>
                  <td className="py-3.5 px-4 text-teal-100/50 text-xs">{order.items} unid.</td>
                  <td className="py-3.5 px-4 text-white font-semibold font-['Cormorant_Garamond',serif]">{fmt(order.total)}</td>
                  <td className="py-3.5 px-4">
                    {editingId === order.id ? (
                      <select
                        defaultValue={order.status}
                        onChange={e => updateStatus(order.id, e.target.value)}
                        className="bg-[#071525] border border-teal-500/30 text-teal-100 text-xs rounded-lg px-2 py-1 outline-none"
                        autoFocus
                        onBlur={() => setEditingId(null)}>
                        {STATUS_OPTIONS.map(s => (
                          <option key={s} value={s} className="bg-[#0d2137] capitalize">{s.replace('_', ' ')}</option>
                        ))}
                      </select>
                    ) : (
                      <button onClick={() => setEditingId(order.id)}
                        className={`text-xs px-2.5 py-1 rounded-full border capitalize cursor-pointer hover:opacity-80 transition-all ${STATUS_STYLES[order.status]}`}
                        title="Clic para cambiar estado">
                        {order.status.replace('_', ' ')}
                        <svg className="inline w-3 h-3 ml-1 opacity-60" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                        </svg>
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {filtered.length === 0 && (
          <div className="text-center py-12 text-teal-400/30">
            <p>No hay pedidos con este estado</p>
          </div>
        )}
      </div>
    </div>
  );
}