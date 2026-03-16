import { useState } from 'react';
import { CartProvider } from './context/CartContext';
import Home from './pages/Home';
import CatalogPage from './pages/CatalogPage';
import CartPage from './pages/CartPage';
import CheckoutPage from './pages/CheckoutPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import AdminPage from './pages/AdminPage';
import AdminLoginPage from './pages/AdminLoginPage';

export default function App() {
  const [page, setPage] = useState('home');

  const renderPage = () => {
    switch (page) {
      case 'catalog':    return <CatalogPage    onNavigate={setPage} />;
      case 'cart':       return <CartPage       onNavigate={setPage} />;
      case 'checkout':   return <CheckoutPage   onNavigate={setPage} />;
      case 'login':      return <LoginPage      onNavigate={setPage} />;
      case 'register':   return <RegisterPage   onNavigate={setPage} />;
      case 'admin':      return <AdminPage      onNavigate={setPage} />;
      case 'admin-login': return <AdminLoginPage onNavigate={setPage} />;
      default:           return <Home           onNavigate={setPage} />;
    }
  };

  return (
    <CartProvider>
      {renderPage()}
    </CartProvider>
  );
}