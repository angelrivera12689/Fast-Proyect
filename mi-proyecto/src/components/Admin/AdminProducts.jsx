import { useState, useEffect } from 'react';
import { getProducts, createProduct, updateProduct, deleteProduct, getActiveCategories, getActiveLaboratories } from '../../services/products';

const fmt = (n) => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n);

const EMPTY = {
  name: '',
  categoryId: '',
  laboratoryId: '',
  basePrice: '',
  stock: '',
  description: '',
  dimensions: '',
  dosage: '',
  expirationDate: '',
  registrationNumber: '',
  activeIngredient: '',
  presentation: '',
  requiresPrescription: false,
  active: true,
};

export default function AdminProducts() {
  const [items, setItems] = useState([]);
  const [categories, setCategories] = useState([]);
  const [laboratories, setLaboratories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [search, setSearch] = useState('');
  const [modal, setModal] = useState(null);
  const [deleteId, setDeleteId] = useState(null);
  const [detailsProduct, setDetailsProduct] = useState(null);
  const [form, setForm] = useState(EMPTY);
  const [formError, setFormError] = useState(null);

  // Cargar datos iniciales
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const [productsData, categoriesData, laboratoriesData] = await Promise.all([
          getProducts(),
          getActiveCategories(),
          getActiveLaboratories()
        ]);
        setItems(productsData);
        setCategories(categoriesData || []);
        setLaboratories(laboratoriesData || []);
        setError(null);
      } catch (err) {
        console.error('Error loading data:', err);
        setError('Error al cargar datos');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const filtered = items.filter(p =>
    p.name.toLowerCase().includes(search.toLowerCase()) ||
    (p.category || p.laboratory || '').toLowerCase().includes(search.toLowerCase())
  );

  const openCreate = () => {
    setForm(EMPTY);
    setFormError(null);
    setModal({ mode: 'create' });
  };

  const openEdit = (p) => {
    setForm({
      name: p.name || '',
      categoryId: p.categoryId || (p.category?.id) || '',
      laboratoryId: p.laboratoryId || (p.laboratory?.id) || '',
      basePrice: String(p.price || ''),
      stock: String(p.stock || ''),
      description: p.description || '',
      dimensions: p.dimensions || '',
      dosage: p.dosage || '',
      expirationDate: p.expirationDate ? new Date(p.expirationDate).toISOString().split('T')[0] : '',
      registrationNumber: p.registrationNumber || '',
      activeIngredient: p.activeIngredient || '',
      presentation: p.presentation || '',
      requiresPrescription: p.requiresPrescription || false,
      active: p.active !== false,
    });
    setFormError(null);
    setModal({ mode: 'edit', id: p.id });
  };

  const handleSave = async () => {
    setSaving(true);
    setFormError(null);
    
    const productData = {
      name: form.name,
      categoryId: form.categoryId ? Number(form.categoryId) : null,
      laboratoryId: form.laboratoryId ? Number(form.laboratoryId) : null,
      basePrice: Number(form.basePrice),
      stock: Number(form.stock),
      description: form.description || null,
      dimensions: form.dimensions || null,
      dosage: form.dosage || null,
      expirationDate: form.expirationDate ? form.expirationDate : null,
      registrationNumber: form.registrationNumber || null,
      activeIngredient: form.activeIngredient || null,
      presentation: form.presentation || null,
      requiresPrescription: form.requiresPrescription || false,
      active: form.active !== false,
    };

    try {
      if (modal.mode === 'create') {
        await createProduct(productData);
      } else {
        await updateProduct(modal.id, productData);
      }
      
      // Recargar datos completos para ver los cambios
      const [productsData, categoriesData, laboratoriesData] = await Promise.all([
        getProducts(),
        getActiveCategories(),
        getActiveLaboratories()
      ]);
      setItems(productsData);
      setCategories(categoriesData || []);
      setLaboratories(laboratoriesData || []);
      setModal(null);
    } catch (err) {
      setFormError(err.message || 'Error al guardar producto');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    try {
      await deleteProduct(deleteId);
      setItems(prev => prev.filter(p => p.id !== deleteId));
      setDeleteId(null);
    } catch (err) {
      alert(err.message || 'Error al eliminar producto');
    }
  };

  const isValid = form.name && form.basePrice && form.stock;

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

      {error && (
        <div className="bg-red-500/10 border border-red-500/30 rounded-xl p-4 text-red-400 text-sm">
          {error}
        </div>
      )}

      {/* Search */}
      <div className="flex items-center gap-2 bg-[#0d2137] border border-teal-500/20 rounded-xl px-4 py-2.5 max-w-sm">
        <svg className="w-4 h-4 text-teal-500/40" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
        <input value={search} onChange={e => setSearch(e.target.value)}
          placeholder="Buscar producto..."
          className="bg-transparent text-sm text-teal-100 placeholder-teal-500/30 outline-none w-full" />
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
              <thead>
                <tr className="border-b border-teal-500/10 bg-teal-500/5">
                  <th className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">Nombre</th>
                  <th className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">Categoría</th>
                  <th className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">Laboratorio</th>
                  <th className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">Precio</th>
                  <th className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">Stock</th>
                  <th className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">Detalles</th>
                  <th className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">Estado</th>
                  <th className="text-left text-teal-400/50 text-xs uppercase tracking-wider py-3.5 px-4 font-normal">Acciones</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map(p => (
                  <tr key={p.id} className="border-b border-teal-500/5 hover:bg-teal-500/4 transition-colors">
                    <td className="py-3 px-4 text-white font-medium max-w-[200px] truncate">{p.name}</td>
                    <td className="py-3 px-4 text-teal-100/50">{p.category || 'Sin categoría'}</td>
                    <td className="py-3 px-4 text-teal-100/50">{p.laboratory || '-'}</td>
                    <td className="py-3 px-4 text-teal-300 font-['Cormorant_Garamond',serif] font-semibold">{fmt(p.price)}</td>
                    <td className="py-3 px-4">
                      <span className={`text-xs font-medium ${p.stock === 0 ? 'text-red-400' : p.stock < 100 ? 'text-amber-400' : 'text-teal-400'}`}>
                        {p.stock}
                      </span>
                    </td>
                    <td className="py-3 px-4">
                      <button onClick={() => setDetailsProduct(p)}
                        className="text-xs px-2 py-1 rounded-lg border border-teal-500/20 text-teal-400/60 hover:text-teal-300 hover:border-teal-400/40 transition-all">
                        Ver
                      </button>
                    </td>
                    <td className="py-3 px-4">
                      <span className={`text-xs px-2 py-0.5 rounded-full ${p.active ? 'bg-teal-500/15 text-teal-400' : 'bg-red-500/15 text-red-400'}`}>
                        {p.active ? 'Activo' : 'Inactivo'}
                      </span>
                    </td>
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
      )}

      {/* Create / Edit Modal */}
      {modal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center px-4">
          <div className="absolute inset-0 bg-black/70 backdrop-blur-sm" onClick={() => setModal(null)} />
          <div className="relative bg-gradient-to-br from-[#0d2137] to-[#071525] border border-teal-500/25 rounded-2xl p-7 w-full max-w-lg shadow-[0_0_60px_rgba(20,184,166,0.1)]">
            <h3 className="font-['Cormorant_Garamond',serif] text-2xl font-bold text-white mb-6">
              {modal.mode === 'create' ? 'Nuevo producto' : 'Editar producto'}
            </h3>
            
            {formError && (
              <div className="bg-red-500/10 border border-red-500/30 rounded-xl p-3 mb-4 text-red-400 text-sm">
                {formError}
              </div>
            )}
            
            <div className="grid grid-cols-2 gap-4">
              <div className="col-span-2">
                <label className="text-teal-400/60 text-xs mb-1.5 block">Nombre del producto *</label>
                <input value={form.name} onChange={e => setForm(f => ({ ...f, name: e.target.value }))}
                  placeholder="Amoxicilina 500mg x 100 cáps"
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
              </div>
              
              <div>
                <label className="text-teal-400/60 text-xs mb-1.5 block">Categoría</label>
                <select value={form.categoryId} onChange={e => setForm(f => ({ ...f, categoryId: e.target.value }))}
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 outline-none focus:border-teal-400/50 transition-colors">
                  <option value="">Seleccionar...</option>
                  {categories.map(c => (
                    <option key={c.id} value={c.id} className="bg-[#0d2137]">{c.name}</option>
                  ))}
                </select>
              </div>
              
                  <div>
                    <label className="text-teal-400/60 text-xs mb-1.5 block">Laboratorio</label>
                    <select 
                      value={form.laboratoryId || ''} 
                      onChange={e => setForm(f => ({ ...f, laboratoryId: e.target.value }))}
                      className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 outline-none focus:border-teal-400/50 transition-colors">
                      <option value="">Seleccionar...</option>
                      {laboratories.map(l => (
                        <option key={l.id} value={l.id} className="bg-[#0d2137]">{l.name}</option>
                      ))}
                    </select>
                  </div>
              
              <div>
                <label className="text-teal-400/60 text-xs mb-1.5 block">Precio (COP) *</label>
                <input type="number" value={form.basePrice} onChange={e => setForm(f => ({ ...f, basePrice: e.target.value }))}
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
                <label className="text-teal-400/60 text-xs mb-1.5 block">Estado</label>
                <select value={form.active ? 'true' : 'false'} onChange={e => setForm(f => ({ ...f, active: e.target.value === 'true' }))}
                  className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 outline-none focus:border-teal-400/50 transition-colors">
                  <option value="true" className="bg-[#0d2137]">Activo</option>
                  <option value="false" className="bg-[#0d2137]">Inactivo</option>
                </select>
              </div>
            </div>

            {/* Campos adicionales para medicamentos */}
            <div className="mt-4 pt-4 border-t border-teal-500/10">
              <p className="text-teal-400/40 text-xs mb-3">Información adicional del medicamento</p>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-teal-400/60 text-xs mb-1.5 block">Dimensiones</label>
                  <input value={form.dimensions} onChange={e => setForm(f => ({ ...f, dimensions: e.target.value }))}
                    placeholder="10x15x5 cm"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                </div>

                <div>
                  <label className="text-teal-400/60 text-xs mb-1.5 block">Dosis</label>
                  <input value={form.dosage} onChange={e => setForm(f => ({ ...f, dosage: e.target.value }))}
                    placeholder="500mg, 10ml, 100UI"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                </div>

                <div className="col-span-2">
                  <label className="text-teal-400/60 text-xs mb-1.5 block">Fecha de vencimiento</label>
                  <input type="date" value={form.expirationDate} onChange={e => setForm(f => ({ ...f, expirationDate: e.target.value }))}
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 outline-none focus:border-teal-400/50 transition-colors" />
                </div>

                <div>
                  <label className="text-teal-400/60 text-xs mb-1.5 block">Registro INVIMA</label>
                  <input value={form.registrationNumber} onChange={e => setForm(f => ({ ...f, registrationNumber: e.target.value }))}
                    placeholder="INVIMA-12345"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                </div>

                <div>
                  <label className="text-teal-400/60 text-xs mb-1.5 block">Principio activo</label>
                  <input value={form.activeIngredient} onChange={e => setForm(f => ({ ...f, activeIngredient: e.target.value }))}
                    placeholder="Amoxicilina"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                </div>

                <div>
                  <label className="text-teal-400/60 text-xs mb-1.5 block">Presentación</label>
                  <input value={form.presentation} onChange={e => setForm(f => ({ ...f, presentation: e.target.value }))}
                    placeholder="Cápsulas, Tabletas"
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 placeholder-teal-500/25 outline-none focus:border-teal-400/50 transition-colors" />
                </div>

                <div>
                  <label className="text-teal-400/60 text-xs mb-1.5 block">Requiere receta</label>
                  <select value={form.requiresPrescription ? 'true' : 'false'} onChange={e => setForm(f => ({ ...f, requiresPrescription: e.target.value === 'true' }))}
                    className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 outline-none focus:border-teal-400/50 transition-colors">
                    <option value="false" className="bg-[#0d2137]">No</option>
                    <option value="true" className="bg-[#0d2137]">Sí</option>
                  </select>
                </div>
              </div>
            </div>
            
            <div className="flex gap-3 mt-6">
              <button onClick={() => setModal(null)}
                className="flex-1 py-2.5 border border-teal-500/20 text-teal-300/60 rounded-xl text-sm hover:bg-teal-500/8 transition-all">
                Cancelar
              </button>
              <button onClick={handleSave} disabled={!isValid || saving}
                className={`flex-1 py-2.5 rounded-xl text-sm font-semibold transition-all ${isValid && !saving ? 'bg-gradient-to-r from-teal-500 to-cyan-500 text-white hover:shadow-[0_0_20px_rgba(20,184,166,0.35)]' : 'bg-teal-500/10 text-teal-500/30 cursor-not-allowed'}`}>
                {saving ? 'Guardando...' : modal.mode === 'create' ? 'Crear producto' : 'Guardar cambios'}
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

      {/* Details Modal */}
      {detailsProduct && (
        <div className="fixed inset-0 z-50 flex items-center justify-center px-4">
          <div className="absolute inset-0 bg-black/70 backdrop-blur-sm" onClick={() => setDetailsProduct(null)} />
          <div className="relative bg-gradient-to-br from-[#0d2137] to-[#071525] border border-teal-500/25 rounded-2xl p-7 w-full max-w-lg shadow-[0_0_60px_rgba(20,184,166,0.1)]">
            <h3 className="font-['Cormorant_Garamond',serif] text-2xl font-bold text-white mb-6">
              Detalles del producto
            </h3>

            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-teal-400/60 text-xs mb-1 block">Nombre</label>
                  <p className="text-white text-sm">{detailsProduct.name}</p>
                </div>
                <div>
                  <label className="text-teal-400/60 text-xs mb-1 block">Categoría</label>
                  <p className="text-white text-sm">{detailsProduct.category || 'Sin categoría'}</p>
                </div>
                <div>
                  <label className="text-teal-400/60 text-xs mb-1 block">Laboratorio</label>
                  <p className="text-white text-sm">{detailsProduct.laboratory || '-'}</p>
                </div>
                <div>
                  <label className="text-teal-400/60 text-xs mb-1 block">Precio</label>
                  <p className="text-teal-300 font-semibold">{fmt(detailsProduct.price)}</p>
                </div>
                <div>
                  <label className="text-teal-400/60 text-xs mb-1 block">Stock</label>
                  <p className="text-white text-sm">{detailsProduct.stock}</p>
                </div>
                <div>
                  <label className="text-teal-400/60 text-xs mb-1 block">SKU</label>
                  <p className="text-white text-sm">{detailsProduct.sku || '-'}</p>
                </div>
              </div>

              <div className="border-t border-teal-500/10 pt-4 mt-4">
                <p className="text-teal-400/40 text-xs mb-3">Información de medicamento</p>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-teal-400/60 text-xs mb-1 block">Dimensiones</label>
                    <p className="text-white text-sm">{detailsProduct.dimensions || '-'}</p>
                  </div>
                  <div>
                    <label className="text-teal-400/60 text-xs mb-1 block">Dosis</label>
                    <p className="text-white text-sm">{detailsProduct.dosage || '-'}</p>
                  </div>
                  <div>
                    <label className="text-teal-400/60 text-xs mb-1 block">Fecha de vencimiento</label>
                    <p className="text-white text-sm">{detailsProduct.expirationDate || '-'}</p>
                  </div>
                  <div>
                    <label className="text-teal-400/60 text-xs mb-1 block">Registro INVIMA</label>
                    <p className="text-white text-sm">{detailsProduct.registrationNumber || '-'}</p>
                  </div>
                  <div>
                    <label className="text-teal-400/60 text-xs mb-1 block">Principio activo</label>
                    <p className="text-white text-sm">{detailsProduct.activeIngredient || '-'}</p>
                  </div>
                  <div>
                    <label className="text-teal-400/60 text-xs mb-1 block">Presentación</label>
                    <p className="text-white text-sm">{detailsProduct.presentation || '-'}</p>
                  </div>
                </div>
              </div>

              {detailsProduct.description && (
                <div className="border-t border-teal-500/10 pt-4 mt-4">
                  <label className="text-teal-400/60 text-xs mb-1 block">Descripción</label>
                  <p className="text-white text-sm">{detailsProduct.description}</p>
                </div>
              )}
            </div>

            <div className="flex gap-3 mt-6">
              <button onClick={() => setDetailsProduct(null)}
                className="flex-1 py-2.5 border border-teal-500/20 text-teal-300/60 rounded-xl text-sm hover:bg-teal-500/8 transition-all">
                Cerrar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
