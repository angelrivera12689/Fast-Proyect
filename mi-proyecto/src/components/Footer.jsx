export default function Footer({ onNavigate }) {
  const nav = onNavigate || (() => {});

  const handleNav = (target) => {
    if (target === 'nosotros' || target === 'contacto') {
      const element = document.getElementById(target);
      if (element) {
        element.scrollIntoView({ behavior: 'smooth' });
      }
    } else {
      nav(target);
    }
  };

  const navLinks = [
    { label: 'Inicio', target: 'home' },
    { label: 'Productos', target: 'catalog' },
    { label: 'Categorías', target: 'catalog' },
    { label: 'Nosotros', target: 'nosotros' },
    { label: 'Contacto', target: 'contacto' }
  ];
 
  return (
    <footer className="bg-[#050d1a] border-t border-teal-500/15">
      <div className="container mx-auto px-8 py-10 flex flex-col md:flex-row items-center justify-between gap-6">
 
        <div className="flex flex-col gap-1 items-center md:items-start">
          <p className="text-2xl font-bold text-white tracking-widest font-['Cormorant_Garamond',serif]">FAST</p>
          <p className="text-teal-400/40 text-xs font-light tracking-wide">Distribuidora de Medicamentos</p>
        </div>
 
        <ul className="flex flex-wrap justify-center gap-6">
          {navLinks.map((item) => (
            <li key={item.label}>
              <button onClick={() => handleNav(item.target)} className="text-teal-100/50 hover:text-teal-300 text-sm font-light tracking-wide transition-colors duration-300">
                {item.label}
              </button>
            </li>
          ))}
        </ul>
 
        <div className="flex flex-col items-center md:items-end gap-3">
          <div className="flex gap-3">
            {['LI', 'WA', 'FB'].map((s) => (
              <a key={s} href="#"
                className="w-8 h-8 rounded-full border border-teal-500/25 flex items-center justify-center text-teal-400/60 hover:border-teal-400 hover:text-teal-300 text-xs font-bold transition-all duration-300">
                {s}
              </a>
            ))}
          </div>
          <p className="text-teal-400/30 text-xs">© 2026 FAST. Todos los derechos reservados.</p>
        </div>
 
      </div>
    </footer>
  );
}