import { useState } from 'react';
import { products as initialProducts, CATEGORIES, LABS } from '../../services/Productsdata';

const fmt = (n) => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n);

const EMPTY = { name: '', category: '', lab: '', price: '', stock: '', minOrder: '' };

export default function AdminProducts() {
  const [items, setItems] = useState(initialProducts);
  const [search, setSearch] = useState('');
  const [modal, setModal] = useState(null); // null | { mode: 'create'|'edit', data }
  const [deleteId, setDeleteId] = useState(null);
  const [form, setForm] = useState(EMPTY);

  const filtered = items.filter(p =>
    p.name.toLowerCase().includes(search.toLowerCase()) ||
    p.category.toLowerCase().includes(search.toLowerCase())
  );

  const openCreate = () => { setForm(EMPTY); setModal({ mode: 'create' }); };
  const openEdit   = (p)  => { setForm({ ...p, price: String(p.price), stock: String(p.stock), minOrder: String(p.minOrder) }); setModal({ mode: 'edit', id: p.id }); };

  const handleSave = () => {
    const parsed = { ...form, price: Number(form.price), stock: Number(form.stock), minOrder: Number(form.minOrder) };
    if (modal.mode === 'create') {
      setItems(prev => [...prev, { ...parsed, id: Date.now() }]);
    } else {
      setItems(prev => prev.map(p => p.id === modal.id ? { ...parsed, id: modal.id } : p));
    }
    setModal(null);
  };

  const handleDelete = () => {
    setItems(prev => prev.filter(p => p.id !== deleteId));
    setDeleteId(null);
  };

  const isValid = form.name && form.category && form.lab && form.price && form.stock && form.minOrder;

  return (
    <div className="flex flex-col gap-6">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h2 className="font-['Cormorant_Garamond',serif] text-3xl font-bold text-white">Productos</h2>
          <p className="text-teal-400/50 text-sm mt-1">{items.length} productos en catálogo</p>
        </div>
        <button onClick={openCreate}
          className="flex items-center gap-2 bg-gradient-to-r from-teal-500 to-cyan-500 text-white px-5 py-2.5 rounded-xl text-sm font-semibold hover:shadow-[0_0_20px_rgba(20,184,166,0.4)] transition-all">
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          Nuevo producto
        </button>
      </div>

      {/* Search */}
      <div className="flex items-center gap-2 bg-[#0d2137] border border-teal-500/20 rounded-xl px-4 py-2.5 max-w-sm">
        <svg className="w-4 h-4 text-teal-500/40" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
        <input value={search} onChange={e => setSearch(e.target.value)}
          placeholder="Buscar producto o categoría..."
          className="bg-transparent text-sm text-teal-100 placeholder-teal-500/30 outline-none w-full" />
      </div>

      {/* Table */}
      <div className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-teal-500/10 bg-teal-500/5">
                {['Nombre', 'Categoría', 'Laboratorio', 'Precio', 'Stock', 'Mín.', 'Acciones'].map(h => (
                  <th key={h} className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {filtered.map(p => (
                <tr key={p.id} className="border-b border-teal-500/5 hover:bg-teal-500/4 transition-colors">
                  <td className="py-3 px-4 text-white font-medium max-w-[200px] truncate">{p.name}</td>
                  <td className="py-3 px-4">
                    <span className="text-xs bg-teal-500/10 text-teal-400/80 px-2 py-0.5 rounded-full">{p.category}</span>
                  </td>
                  <td className="py-3 px-4 text-teal-100/50">{p.lab}</td>
                  <td className="py-3 px-4 text-teal-300 font-['Cormorant_Garamond',serif] font-semibold">{fmt(p.price)}</td>
                  <td className="py-3 px-4">
                    <span className={`text-xs font-medium ${p.stock === 0 ? 'text-red-400' : p.stock < 100 ? 'text-amber-400' : 'text-teal-400'}`}>
                      {p.stock}
                    </span>
                  </td>
                  <td className="py-3 px-4 text-teal-100/40 text-xs">{p.minOrder}</td>
                  <td className="py-3 px-4">
                    <div className="flex gap-2">
                      <button onClick={() => openEdit(p)}
                        className="p-1.5 rounded-lg border border-teal-500/20 text-teal-400/60 hover:text-teal-300 hover:border-teal-400/40 transition-all">
                        <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                        </svg>
                      </button>
                      <button onClick={() => setDeleteId(p.id)}
                        className="p-1.5 rounded-lg border border-red-500/20 text-red-400/50 hover:text-red-400 hover:border-red-400/40 transition-all">
                        <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                        </svg>
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Create / Edit Modal */}
      {modal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center px-4">
          <div className="absolute inset-0 bg-black/70 backdrop-blur-sm" onClick={() => setModal(null)} />
          <div className="relative bg-gradient-to-br from-[#0d2137] to-[#071525] border border-teal-500/25 rounded-2xl p-7 w-full max-w-lg shadow-[0_0_60px_rgba(20,184,166,0.1)]">
            <h3 className="font-['Cormorant_Garamond',serif] text-2xl font-bold text-white mb-6">
              {modal.mode === 'create' ? 'Nuevo producto' : 'Editar producto'}
            </h3>
            <div className="grid grid-cols-2 gap-4">
              <div className="col-span-2">
                <label className="text-teal-400/60 text-xs mb-1.5 block">Nombre del producto *</label>
                <input value={form.name} onChange={e => setForm(f => ({ ...f, name: e.target.value }))}
                  placeholder="Amoxicilina 500mg x 100 cáps"
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
              </div>
              <div>
                <label className="text-teal-400/60 text-xs mb-1.5 block">Categoría *</label>
                <select value={form.category} onChange={e => setForm(f => ({ ...f, category: e.target.value }))}
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 outline-none focus:border-teal-400/50 transition-colors">
                  <option value="">Seleccionar...</option>
                  {CATEGORIES.filter(c => c !== 'Todos').map(c => <option key={c} value={c} className="bg-[#0d2137]">{c}</option>)}
                </select>
              </div>
              <div>
                <label className="text-teal-400/60 text-xs mb-1.5 block">Laboratorio *</label>
                <select value={form.lab} onChange={e => setForm(f => ({ ...f, lab: e.target.value }))}
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 outline-none focus:border-teal-400/50 transition-colors">
                  <option value="">Seleccionar...</option>
                  {LABS.filter(l => l !== 'Todos').map(l => <option key={l} value={l} className="bg-[#0d2137]">{l}</option>)}
                </select>
              </div>
              <div>
                <label className="text-teal-400/60 text-xs mb-1.5 block">Precio (COP) *</label>
                <input type="number" value={form.price} onChange={e => setForm(f => ({ ...f, price: e.target.value }))}
                  placeholder="18500"
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
              </div>
              <div>
                <label className="text-teal-400/60 text-xs mb-1.5 block">Stock *</label>
                <input type="number" value={form.stock} onChange={e => setForm(f => ({ ...f, stock: e.target.value }))}
                  placeholder="100"
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
              </div>
              <div>
                <label className="text-teal-400/60 text-xs mb-1.5 block">Pedido mínimo *</label>
                <input type="number" value={form.minOrder} onChange={e => setForm(f => ({ ...f, minOrder: e.target.value }))}
                  placeholder="10"
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
              </div>
            </div>
            <div className="flex gap-3 mt-6">
              <button onClick={() => setModal(null)}
                className="flex-1 py-2.5 border border-teal-500/20 text-teal-300/60 rounded-xl text-sm hover:bg-teal-500/8 transition-all">
                Cancelar
              </button>
              <button onClick={handleSave} disabled={!isValid}
                className={`flex-1 py-2.5 rounded-xl text-sm font-semibold transition-all ${isValid ? 'bg-gradient-to-r from-teal-500 to-cyan-500 text-white hover:shadow-[0_0_20px_rgba(20,184,166,0.35)]' : 'bg-teal-500/10 text-teal-500/30 cursor-not-allowed'}`}>
                {modal.mode === 'create' ? 'Crear producto' : 'Guardar cambios'}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Delete confirm */}
      {deleteId && (
        <div className="fixed inset-0 z-50 flex items-center justify-center px-4">
          <div className="absolute inset-0 bg-black/70 backdrop-blur-sm" onClick={() => setDeleteId(null)} />
          <div className="relative bg-gradient-to-br from-[#0d2137] to-[#071525] border border-red-500/25 rounded-2xl p-7 w-full max-w-sm text-center">
            <div className="w-12 h-12 rounded-full bg-red-500/15 border border-red-500/25 flex items-center justify-center mx-auto mb-4">
              <svg className="w-6 h-6 text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <p className="text-white font-semibold mb-2">¿Eliminar producto?</p>
            <p className="text-teal-100/40 text-sm mb-6">Esta acción no se puede deshacer.</p>
            <div className="flex gap-3">
              <button onClick={() => setDeleteId(null)} className="flex-1 py-2.5 border border-teal-500/20 text-teal-300/60 rounded-xl text-sm hover:bg-teal-500/8 transition-all">Cancelar</button>
              <button onClick={handleDelete} className="flex-1 py-2.5 bg-red-500/20 text-red-400 border border-red-500/30 rounded-xl text-sm font-semibold hover:bg-red-500/30 transition-all">Eliminar</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}