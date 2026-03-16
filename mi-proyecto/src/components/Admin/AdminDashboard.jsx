import { salesByMonth, topProducts, mockOrders } from '../../services/adminData';

const fmt = (n) => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n);

const STATUS_STYLES = {
  entregado:    'bg-teal-500/15 text-teal-400 border-teal-500/25',
  en_tránsito:  'bg-amber-500/15 text-amber-400 border-amber-500/25',
  pendiente:    'bg-blue-500/15 text-blue-400 border-blue-500/25',
};

const STAT_CARDS = [
  {
    label: 'Ventas del mes',
    value: fmt(3595000),
    delta: '+14%',
    up: true,
    icon: <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>,
    accent: 'from-teal-400 to-cyan-400',
  },
  {
    label: 'Pedidos este mes',
    value: '6',
    delta: '+2',
    up: true,
    icon: <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" /></svg>,
    accent: 'from-amber-400 to-yellow-300',
  },
  {
    label: 'Clientes activos',
    value: '5',
    delta: '+1',
    up: true,
    icon: <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" /></svg>,
    accent: 'from-cyan-400 to-teal-300',
  },
  {
    label: 'Productos en catálogo',
    value: '20',
    delta: 'estable',
    up: null,
    icon: <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" /></svg>,
    accent: 'from-purple-400 to-indigo-400',
  },
];

const maxSale = Math.max(...salesByMonth.map(m => m.value));

export default function AdminDashboard() {
  const recentOrders = mockOrders.slice(0, 5);

  return (
    <div className="flex flex-col gap-8">
      <div>
        <h2 className="font-['Cormorant_Garamond',serif] text-3xl font-bold text-white">Dashboard</h2>
        <p className="text-teal-400/50 text-sm mt-1">Resumen general · Marzo 2026</p>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-2 xl:grid-cols-4 gap-4">
        {STAT_CARDS.map((card, i) => (
          <div key={i} className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-5 hover:border-teal-400/25 transition-all duration-300">
            <div className="flex items-start justify-between mb-4">
              <div className={`w-9 h-9 rounded-xl bg-gradient-to-br ${card.accent} bg-opacity-10 flex items-center justify-center`}
                style={{ background: 'rgba(13,33,55,0.8)', border: '1px solid rgba(255,255,255,0.06)' }}>
                <span className={`text-transparent bg-clip-text bg-gradient-to-br ${card.accent}`}>{card.icon}</span>
              </div>
              {card.up !== null && (
                <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${card.up ? 'bg-teal-500/15 text-teal-400' : 'bg-red-500/15 text-red-400'}`}>
                  {card.delta}
                </span>
              )}
            </div>
            <p className="font-['Cormorant_Garamond',serif] text-2xl font-bold text-white">{card.value}</p>
            <p className="text-teal-100/40 text-xs mt-1">{card.label}</p>
          </div>
        ))}
      </div>

      <div className="grid lg:grid-cols-2 gap-6">
        {/* Sales bar chart */}
        <div className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-6">
          <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-6">Ventas últimos 6 meses</p>
          <div className="flex items-end gap-3 h-40">
            {salesByMonth.map((m, i) => {
              const pct = (m.value / maxSale) * 100;
              const isLast = i === salesByMonth.length - 1;
              return (
                <div key={m.month} className="flex-1 flex flex-col items-center gap-2">
                  <p className="text-teal-400/50 text-xs">{fmt(m.value).replace('$', '').replace(',00', '')}</p>
                  <div className="w-full rounded-t-lg transition-all duration-500 relative group"
                    style={{
                      height: `${pct}%`,
                      minHeight: '8px',
                      background: isLast
                        ? 'linear-gradient(to top, #0d9488, #22d3ee)'
                        : 'rgba(20,184,166,0.25)',
                      border: isLast ? '1px solid rgba(20,184,166,0.5)' : '1px solid rgba(20,184,166,0.15)',
                    }}>
                  </div>
                  <p className={`text-xs font-medium ${isLast ? 'text-teal-300' : 'text-teal-100/40'}`}>{m.month}</p>
                </div>
              );
            })}
          </div>
        </div>

        {/* Top products */}
        <div className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-6">
          <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-6">Productos más pedidos</p>
          <div className="flex flex-col gap-4">
            {topProducts.map((p, i) => {
              const maxOrders = topProducts[0].orders;
              const pct = (p.orders / maxOrders) * 100;
              return (
                <div key={p.name}>
                  <div className="flex justify-between items-center mb-1.5">
                    <div className="flex items-center gap-2">
                      <span className="text-teal-500/40 text-xs font-bold w-4">{i + 1}</span>
                      <span className="text-teal-100/70 text-sm">{p.name}</span>
                    </div>
                    <div className="text-right">
                      <span className="text-teal-300 text-xs font-semibold">{p.orders} pedidos</span>
                    </div>
                  </div>
                  <div className="h-1.5 bg-teal-500/10 rounded-full overflow-hidden">
                    <div className="h-full rounded-full bg-gradient-to-r from-teal-500 to-cyan-400 transition-all duration-700"
                      style={{ width: `${pct}%` }} />
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>

      {/* Recent orders */}
      <div className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-6">
        <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-5">Pedidos recientes</p>
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-teal-500/10">
                {['ID', 'Cliente', 'Fecha', 'Total', 'Items', 'Estado'].map(h => (
                  <th key={h} className="text-left text-teal-400/40 text-xs uppercase tracking-widest pb-3 pr-4 font-normal">{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {recentOrders.map(order => (
                <tr key={order.id} className="border-b border-teal-500/5 hover:bg-teal-500/3 transition-colors">
                  <td className="py-3 pr-4 text-teal-300 font-mono text-xs">{order.id}</td>
                  <td className="py-3 pr-4 text-teal-100/70">{order.client}</td>
                  <td className="py-3 pr-4 text-teal-100/40 text-xs">{order.date}</td>
                  <td className="py-3 pr-4 text-white font-semibold font-['Cormorant_Garamond',serif]">{fmt(order.total)}</td>
                  <td className="py-3 pr-4 text-teal-100/50 text-xs">{order.items} unid.</td>
                  <td className="py-3">
                    <span className={`text-xs px-2.5 py-1 rounded-full border capitalize ${STATUS_STYLES[order.status]}`}>
                      {order.status.replace('_', ' ')}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}