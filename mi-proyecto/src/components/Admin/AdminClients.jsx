import { useState } from 'react';
import { heroContent as initial } from '../../services/adminData';

const Field = ({ label, name, value, onChange, multiline }) => (
  <div>
    <label className="text-teal-400/60 text-xs mb-1.5 block tracking-wide">{label}</label>
    {multiline ? (
      <textarea name={name} value={value} onChange={onChange} rows={3}
        className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 outline-none focus:border-teal-400/50 transition-colors resize-none" />
    ) : (
      <input name={name} value={value} onChange={onChange}
        className="w-full bg-[#071525] border border-teal-500/20 rounded-xl px-4 py-2.5 text-sm text-teal-100 outline-none focus:border-teal-400/50 transition-colors" />
    )}
  </div>
);

export default function AdminHero() {
  const [content, setContent] = useState(initial);
  const [saved, setSaved] = useState(false);

  const handleChange = (e) => {
    setSaved(false);
    setContent(c => ({ ...c, [e.target.name]: e.target.value }));
  };

  const handleSave = () => {
    // TODO: enviar al backend
    setSaved(true);
    setTimeout(() => setSaved(false), 3000);
  };

  return (
    <div className="flex flex-col gap-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="font-['Cormorant_Garamond',serif] text-3xl font-bold text-white">Contenido Landing</h2>
          <p className="text-teal-400/50 text-sm mt-1">Edita los textos y estadísticas visibles en la página principal</p>
        </div>
        <button onClick={handleSave}
          className={`flex items-center gap-2 px-5 py-2.5 rounded-xl text-sm font-semibold transition-all duration-300
            ${saved
              ? 'bg-teal-500/20 text-teal-300 border border-teal-400/30'
              : 'bg-gradient-to-r from-teal-500 to-cyan-500 text-white hover:shadow-[0_0_20px_rgba(20,184,166,0.4)]'}`}>
          {saved ? (
            <><svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" /></svg>Guardado</>
          ) : (
            <><svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7H5a2 2 0 00-2 2v9a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-3m-1 4l-3 3m0 0l-3-3m3 3V4" /></svg>Guardar cambios</>
          )}
        </button>
      </div>

      <div className="grid lg:grid-cols-2 gap-6">
        {/* Hero texts */}
        <div className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-6 flex flex-col gap-4">
          <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold">Textos del Hero</p>
          <Field label="Badge superior"       name="badge"       value={content.badge}       onChange={handleChange} />
          <Field label="Título línea 1"        name="title1"      value={content.title1}      onChange={handleChange} />
          <Field label="Título línea 2"        name="title2"      value={content.title2}      onChange={handleChange} />
          <Field label="Texto destacado 1 (teal)"   name="highlight1"  value={content.highlight1}  onChange={handleChange} />
          <Field label="Texto destacado 2 (amber)"  name="highlight2"  value={content.highlight2}  onChange={handleChange} />
          <Field label="Descripción"           name="description" value={content.description} onChange={handleChange} multiline />
          <Field label="Texto del botón CTA"   name="cta"         value={content.cta}         onChange={handleChange} />
        </div>

        {/* Stats + preview */}
        <div className="flex flex-col gap-6">
          <div className="bg-gradient-to-br from-[#0d2137]/80 to-[#071525]/80 border border-teal-500/15 rounded-2xl p-6 flex flex-col gap-4">
            <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold">Estadísticas</p>
            <div className="grid grid-cols-2 gap-4">
              <Field label="Stat 1 — Valor" name="stat1Value" value={content.stat1Value} onChange={handleChange} />
              <Field label="Stat 1 — Etiqueta" name="stat1Label" value={content.stat1Label} onChange={handleChange} />
              <Field label="Stat 2 — Valor" name="stat2Value" value={content.stat2Value} onChange={handleChange} />
              <Field label="Stat 2 — Etiqueta" name="stat2Label" value={content.stat2Label} onChange={handleChange} />
              <Field label="Stat 3 — Valor" name="stat3Value" value={content.stat3Value} onChange={handleChange} />
              <Field label="Stat 3 — Etiqueta" name="stat3Label" value={content.stat3Label} onChange={handleChange} />
            </div>
          </div>

          {/* Live preview */}
          <div className="bg-gradient-to-br from-[#071525] to-[#050d1a] border border-teal-500/15 rounded-2xl p-6">
            <p className="text-teal-300 text-xs tracking-[0.2em] uppercase font-semibold mb-4">Vista previa</p>
            <div className="space-y-2">
              <p className="text-teal-400 text-xs tracking-widest uppercase">{content.badge}</p>
              <p className="font-['Cormorant_Garamond',serif] text-2xl font-bold text-white leading-tight">
                {content.title1} {content.title2}{' '}
                <span className="text-transparent bg-clip-text bg-gradient-to-r from-teal-300 to-cyan-400 italic">{content.highlight1} </span>
                <span className="text-transparent bg-clip-text bg-gradient-to-r from-amber-300 to-yellow-200 italic">{content.highlight2}</span>
              </p>
              <p className="text-teal-100/50 text-xs leading-relaxed">{content.description}</p>
              <div className="flex gap-5 pt-1">
                {[
                  [content.stat1Value, content.stat1Label],
                  [content.stat2Value, content.stat2Label],
                  [content.stat3Value, content.stat3Label],
                ].map(([v, l], i) => (
                  <div key={i}>
                    <p className="text-white font-bold font-['Cormorant_Garamond',serif]">{v}</p>
                    <p className="text-teal-400/50 text-xs">{l}</p>
                  </div>
                ))}
              </div>
              <div className="inline-block mt-1 px-4 py-1.5 bg-gradient-to-r from-teal-500 to-cyan-500 text-white text-xs rounded-full font-semibold">
                {content.cta}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}