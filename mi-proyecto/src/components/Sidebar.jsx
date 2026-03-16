import { CATEGORIES, LABS } from '../services/Productsdata';

const fmt = (n) => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n);

export default function Sidebar({
  search, setSearch,
  selectedCategory, setSelectedCategory,
  selectedLab, setSelectedLab,
  priceRange, setPriceRange,
  stockFilter, setStockFilter,
  onReset,
}) {
  return (
    <aside className="w-full flex flex-col gap-6">

      {/* Buscar */}
      <div>
        <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-3">Buscar</p>
        <div className="flex items-center gap-2 bg-[#0d2137] border border-teal-500/20 rounded-xl px-3 py-2">
          <svg className="w-4 h-4 text-teal-500/50 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
          <input
            value={search}
            onChange={e => setSearch(e.target.value)}
            placeholder="Nombre del producto..."
            className="bg-transparent text-sm text-teal-100 placeholder-teal-500/30 outline-none w-full"
          />
        </div>
      </div>

      {/* Categoría */}
      <div>
        <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-3">Categoría</p>
        <div className="flex flex-col gap-1.5">
          {CATEGORIES.map(c => (
            <button key={c} onClick={() => setSelectedCategory(c)}
              className={`text-left text-sm px-3 py-1.5 rounded-lg transition-all duration-200
                ${selectedCategory === c
                  ? 'bg-teal-500/20 text-teal-300 border border-teal-500/30'
                  : 'text-teal-100/50 hover:text-teal-200 hover:bg-teal-500/10'}`}>
              {c}
            </button>
          ))}
        </div>
      </div>

      {/* Laboratorio */}
      <div>
        <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-3">Laboratorio</p>
        <div className="flex flex-col gap-1.5">
          {LABS.map(l => (
            <button key={l} onClick={() => setSelectedLab(l)}
              className={`text-left text-sm px-3 py-1.5 rounded-lg transition-all duration-200
                ${selectedLab === l
                  ? 'bg-teal-500/20 text-teal-300 border border-teal-500/30'
                  : 'text-teal-100/50 hover:text-teal-200 hover:bg-teal-500/10'}`}>
              {l}
            </button>
          ))}
        </div>
      </div>

      {/* Precio */}
      <div>
        <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-3">Precio máximo</p>
        <input
          type="range" min={0} max={60000} step={1000}
          value={priceRange[1]}
          onChange={e => setPriceRange([0, Number(e.target.value)])}
          className="w-full accent-teal-400"
        />
        <div className="flex justify-between text-xs text-teal-400/50 mt-1">
          <span>$0</span>
          <span>{fmt(priceRange[1])}</span>
        </div>
      </div>

      {/* Disponibilidad */}
      <div>
        <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-3">Disponibilidad</p>
        {[['todos', 'Todos'], ['disponible', 'En stock'], ['bajo', 'Stock bajo']].map(([val, label]) => (
          <button key={val} onClick={() => setStockFilter(val)}
            className={`w-full text-left text-sm px-3 py-1.5 rounded-lg mb-1 transition-all duration-200
              ${stockFilter === val
                ? 'bg-teal-500/20 text-teal-300 border border-teal-500/30'
                : 'text-teal-100/50 hover:text-teal-200 hover:bg-teal-500/10'}`}>
            {label}
          </button>
        ))}
      </div>

      {/* Limpiar */}
      <button onClick={onReset}
        className="w-full py-2 text-xs tracking-widest uppercase text-teal-400/60 border border-teal-500/20 rounded-xl hover:border-teal-400/40 hover:text-teal-300 transition-all duration-300">
        Limpiar filtros
      </button>

    </aside>
  );
}