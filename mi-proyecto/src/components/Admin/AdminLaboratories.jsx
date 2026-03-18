import { useState, useEffect } from 'react';
import { getActiveLaboratories, createLaboratory, updateLaboratory, deleteLaboratory } from '../../services/products';

const EMPTY = {
  name: '',
  description: '',
  country: '',
  contactEmail: '',
  contactPhone: '',
  active: true,
};

export default function AdminLaboratories() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [search, setSearch] = useState('');
  const [modal, setModal] = useState(null);
  const [deleteId, setDeleteId] = useState(null);
  const [form, setForm] = useState(EMPTY);
  const [formError, setFormError] = useState(null);

  // Cargar datos iniciales - solo laboratorios activos
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const laboratoriesData = await getActiveLaboratories();
        setItems(laboratoriesData || []);
        setError(null);
      } catch (err) {
        console.error('Error loading laboratories:', err);
        setError('Error al cargar laboratorios');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const filtered = items.filter(l =>
    (l.name || '').toLowerCase().includes(search.toLowerCase()) ||
    (l.description || '').toLowerCase().includes(search.toLowerCase()) ||
    (l.country || '').toLowerCase().includes(search.toLowerCase())
  );

  const openCreate = () => {
    setForm(EMPTY);
    setFormError(null);
    setModal({ mode: 'create' });
  };

  const openEdit = (l) => {
    setForm({
      name: l.name || '',
      description: l.description || '',
      country: l.country || '',
      contactEmail: l.contactEmail || '',
      contactPhone: l.contactPhone || '',
      active: l.active !== false,
    });
    setFormError(null);
    setModal({ mode: 'edit', id: l.id });
  };

  const handleSave = async () => {
    if (!form.name?.trim()) {
      setFormError('El nombre es requerido');
      return;
    }

    setSaving(true);
    setFormError(null);

    const laboratoryData = {
      name: form.name.trim(),
      description: form.description?.trim() || null,
      country: form.country?.trim() || null,
      contactEmail: form.contactEmail?.trim() || null,
      contactPhone: form.contactPhone?.trim() || null,
      active: form.active,
    };

    try {
      if (modal.mode === 'create') {
        await createLaboratory(laboratoryData);
      } else {
        await updateLaboratory(modal.id, laboratoryData);
      }
      
      // Recargar datos
      const laboratoriesData = await getActiveLaboratories();
      setItems(laboratoriesData || []);
      setModal(null);
      setForm(EMPTY);
    } catch (err) {
      console.error('Error saving laboratory:', err);
      setFormError(err.message || 'Error al guardar laboratorio');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    setSaving(true);
    try {
      await deleteLaboratory(deleteId);
      const laboratoriesData = await getActiveLaboratories();
      setItems(laboratoriesData || []);
      setDeleteId(null);
    } catch (err) {
      console.error('Error deleting laboratory:', err);
      setError(err.message || 'Error al eliminar laboratorio');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="flex flex-col gap-6">
      {/* Header */}
      <div className="flex items-center justify-between gap-4">
        <div>
          <h2 className="font-['Cormorant_Garamond',serif] text-3xl font-bold text-white">Laboratorios</h2>
          <p className="text-teal-400/50 text-sm mt-1">{items.length} laboratorios activos</p>
        </div>
        <button
          onClick={openCreate}
          className="flex items-center gap-2 bg-gradient-to-r from-teal-500 to-cyan-500 text-white px-5 py-2.5 rounded-xl text-sm font-semibold hover:shadow-[0_0_20px_rgba(20,184,166,0.4)] transition-all">
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          Nuevo laboratorio
        </button>
      </div>

      {/* Error global */}
      {error && (
        <div className="bg-red-500/10 border border-red-500/30 rounded-xl p-4 text-red-400 text-sm">
          {error}
        </div>
      )}

      {/* Buscador */}
      <div className="flex items-center gap-2 bg-[#0d2137] border border-teal-500/20 rounded-xl px-4 py-2.5 max-w-sm">
        <svg className="w-4 h-4 text-teal-500/40" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
        <input
          type="text"
          placeholder="Buscar laboratorios..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="bg-transparent text-sm text-teal-100 placeholder-teal-500/30 outline-none w-full"
        />
      </div>

      {/* Loading / Table */}
      {loading ? (
        <div className="flex justify-center py-12">
          <div className="w-8 h-8 border-2 border-teal-500 border-t-transparent rounded-full animate-spin"></div>
        </div>
      ) : (
        <div className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead className="border-b border-teal-500/10 bg-teal-500/5">
                <tr>
                  <th className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">Nombre</th>
                  <th className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">Descripción</th>
                  <th className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">País</th>
                  <th className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">Estado</th>
                  <th className="text-right text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-teal-500/5">
                {filtered.length === 0 ? (
                  <tr>
                    <td colSpan={5} className="py-8 text-center text-teal-100/40">
                      No se encontraron laboratorios
                    </td>
                  </tr>
                ) : (
                  filtered.map((laboratory) => (
                    <tr key={laboratory.id} className="hover:bg-teal-500/4 transition-colors">
                      <td className="py-3 px-4 text-white font-medium">{laboratory.name}</td>
                      <td className="py-3 px-4 text-teal-100/50 max-w-xs truncate">{laboratory.description || '-'}</td>
                      <td className="py-3 px-4 text-teal-100/50">{laboratory.country || '-'}</td>
                      <td className="py-3 px-4">
                        <span className={`text-xs px-2 py-0.5 rounded-full ${laboratory.active ? 'bg-teal-500/15 text-teal-400' : 'bg-red-500/15 text-red-400'}`}>
                          {laboratory.active ? 'Activo' : 'Inactivo'}
                        </span>
                      </td>
                      <td className="py-3 px-4">
                        <div className="flex gap-2 justify-end">
                          <button
                            onClick={() => openEdit(laboratory)}
                            className="p-1.5 rounded-lg border border-teal-500/20 text-teal-400/60 hover:text-teal-300 hover:border-teal-400/40 transition-all"
                          >
                            <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                            </svg>
                          </button>
                          <button
                            onClick={() => setDeleteId(laboratory.id)}
                            className="p-1.5 rounded-lg border border-red-500/20 text-red-400/50 hover:text-red-400 hover:border-red-400/40 transition-all"
                          >
                            <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                            </svg>
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Modal de crear/editar */}
      {modal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center px-4">
          <div className="absolute inset-0 bg-black/70 backdrop-blur-sm" onClick={() => setModal(null)} />
          <div className="relative bg-gradient-to-br from-[#0d2137] to-[#071525] border border-teal-500/25 rounded-2xl p-7 w-full max-w-lg shadow-[0_0_60px_rgba(20,184,166,0.1)]">
            <h3 className="font-['Cormorant_Garamond',serif] text-2xl font-bold text-white mb-6">
              {modal.mode === 'create' ? 'Nuevo laboratorio' : 'Editar laboratorio'}
            </h3>
            
            {formError && (
              <div className="bg-red-500/10 border border-red-500/30 rounded-xl p-3 mb-4 text-red-400 text-sm">
                {formError}
              </div>
            )}

            <div className="space-y-4">
              <div>
                <label className="text-teal-400/60 text-xs mb-1.5 block">
                  Nombre *
                </label>
                <input
                  type="text"
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors"
                  placeholder="Ej: Genfar"
                />
              </div>

              <div>
                <label className="text-teal-400/60 text-xs mb-1.5 block">
                  Descripción
                </label>
                <textarea
                  value={form.description}
                  onChange={(e) => setForm({ ...form, description: e.target.value })}
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors"
                  rows={3}
                  placeholder="Descripción del laboratorio..."
                />
              </div>

              <div>
                <label className="text-teal-400/60 text-xs mb-1.5 block">
                  País
                </label>
                <input
                  type="text"
                  value={form.country}
                  onChange={(e) => setForm({ ...form, country: e.target.value })}
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors"
                  placeholder="Ej: Colombia"
                />
              </div>

              <div>
                <label className="text-teal-400/60 text-xs mb-1.5 block">
                  Correo de contacto
                </label>
                <input
                  type="email"
                  value={form.contactEmail}
                  onChange={(e) => setForm({ ...form, contactEmail: e.target.value })}
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors"
                  placeholder="contacto@laboratorio.com"
                />
              </div>

              <div>
                <label className="text-teal-400/60 text-xs mb-1.5 block">
                  Teléfono de contacto
                </label>
                <input
                  type="text"
                  value={form.contactPhone}
                  onChange={(e) => setForm({ ...form, contactPhone: e.target.value })}
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors"
                  placeholder="+57 300 123 4567"
                />
              </div>

              <div className="flex items-center gap-3">
                <input
                  type="checkbox"
                  id="active"
                  checked={form.active}
                  onChange={(e) => setForm({ ...form, active: e.target.checked })}
                  className="h-4 w-4 text-teal-500 border-teal-500/30 rounded bg-[#071525] focus:ring-teal-500/50"
                />
                <label htmlFor="active" className="text-sm text-teal-100">
                  Laboratorio activo
                </label>
              </div>
            </div>

            <div className="flex gap-3 mt-6">
              <button
                onClick={() => setModal(null)}
                className="flex-1 py-2.5 border border-teal-500/20 text-teal-300/60 rounded-xl text-sm hover:bg-teal-500/8 transition-all"
              >
                Cancelar
              </button>
              <button
                onClick={handleSave}
                disabled={saving}
                className={`flex-1 py-2.5 rounded-xl text-sm font-semibold transition-all ${saving ? 'bg-teal-500/10 text-teal-500/30 cursor-not-allowed' : 'bg-gradient-to-r from-teal-500 to-cyan-500 text-white hover:shadow-[0_0_20px_rgba(20,184,166,0.35)]'}`}
              >
                {saving ? 'Guardando...' : 'Guardar'}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Modal de confirmar eliminación */}
      {deleteId && (
        <div className="fixed inset-0 z-50 flex items-center justify-center px-4">
          <div className="absolute inset-0 bg-black/70 backdrop-blur-sm" onClick={() => setDeleteId(null)} />
          <div className="relative bg-gradient-to-br from-[#0d2137] to-[#071525] border border-red-500/25 rounded-2xl p-7 w-full max-w-sm text-center">
            <div className="w-12 h-12 rounded-full bg-red-500/15 border border-red-500/25 flex items-center justify-center mx-auto mb-4">
              <svg className="w-6 h-6 text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <p className="text-white font-semibold mb-2">¿Eliminar laboratorio?</p>
            <p className="text-teal-100/40 text-sm mb-6">Esta acción no se puede deshacer.</p>
            <div className="flex gap-3">
              <button 
                onClick={() => setDeleteId(null)} 
                className="flex-1 py-2.5 border border-teal-500/20 text-teal-300/60 rounded-xl text-sm hover:bg-teal-500/8 transition-all"
              >
                Cancelar
              </button>
              <button 
                onClick={handleDelete} 
                disabled={saving}
                className="flex-1 py-2.5 bg-red-500/20 text-red-400 border border-red-500/30 rounded-xl text-sm font-semibold hover:bg-red-500/30 transition-all disabled:opacity-50"
              >
                {saving ? 'Eliminando...' : 'Eliminar'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
