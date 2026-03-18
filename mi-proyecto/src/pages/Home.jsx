import Navbar from '../components/Navbar';
import Hero from '../components/Hero';
import Products from '../components/Products';
import AboutUs from '../components/AboutUs';
import Reviews from '../components/Reviews';
import Footer from '../components/Footer';
import Contact from '../components/Contact';
import { useCart } from '../context/useCart';
 
export default function Home({ onNavigate }) {
  const { count } = useCart();
  return (
    <div className="min-h-screen bg-[#050d1a]">
      <Navbar onNavigate={onNavigate} cartCount={count} />
      <Hero onNavigate={onNavigate} />
      <Products onNavigate={onNavigate} />
      <div id="nosotros">
        <AboutUs />
      </div>
      <Reviews />
      <div id="contacto">
        <Contact />
      </div>
      <Footer onNavigate={onNavigate} />
    </div>
  );
}
