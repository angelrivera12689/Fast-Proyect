import { useState, useEffect } from 'react';
import { getCart, addToCart as addToCartApi, updateCartItem as updateCartItemApi, removeFromCart as removeFromCartApi, clearCart as clearCartApi } from '../services/cart';
import { isAuthenticated } from '../services/auth';
import { CartContext } from './useCart.jsx';

export function CartProvider({ children }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [authCheck, setAuthCheck] = useState(0); // Contador para detectar cambios de auth

  // Cargar carrito del backend al iniciar y cuando cambia la autenticación
  useEffect(() => {
    const loadCart = async () => {
      // Limpiar el carrito local cuando no está autenticado
      if (!isAuthenticated()) {
        setItems([]);
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        const cartItems = await getCart();
        setItems(cartItems);
      } catch (error) {
        console.error('Error loading cart:', error);
        setItems([]);
      } finally {
        setLoading(false);
      }
    };
    loadCart();
  }, [authCheck]); // Se ejecuta cuando cambia authCheck

  const addItem = async (product, qty = 1) => {
    if (!isAuthenticated()) {
      // Si no está autenticado, usar comportamiento local
      setItems(prev => {
        const existing = prev.find(i => i.id === product.id);
        if (existing) {
          return prev.map(i => i.id === product.id ? { ...i, qty: i.qty + qty } : i);
        }
        return [...prev, { ...product, qty }];
      });
      return;
    }

    try {
      // Sincronizar con el backend
      await addToCartApi(product.id, qty);
      
      // Actualizar estado local
      setItems(prev => {
        const existing = prev.find(i => i.id === product.id);
        if (existing) {
          return prev.map(i => i.id === product.id ? { ...i, qty: i.qty + qty } : i);
        }
        return [...prev, { ...product, qty }];
      });
    } catch (error) {
      console.error('Error adding to cart:', error);
      throw error;
    }
  };

  const removeItem = async (id) => {
    if (!isAuthenticated()) {
      setItems(prev => prev.filter(i => i.id !== id));
      return;
    }

    try {
      await removeFromCartApi(id);
      setItems(prev => prev.filter(i => i.id !== id));
    } catch (error) {
      console.error('Error removing from cart:', error);
      throw error;
    }
  };

  const updateQty = async (id, qty) => {
    if (qty < 1) return removeItem(id);

    if (!isAuthenticated()) {
      setItems(prev => prev.map(i => i.id === id ? { ...i, qty } : i));
      return;
    }

    try {
      await updateCartItemApi(id, qty);
      setItems(prev => prev.map(i => i.id === id ? { ...i, qty } : i));
    } catch (error) {
      console.error('Error updating cart:', error);
      throw error;
    }
  };

  const clearCart = async () => {
    if (!isAuthenticated()) {
      setItems([]);
      return;
    }

    try {
      await clearCartApi();
      setItems([]);
    } catch (error) {
      console.error('Error clearing cart:', error);
      throw error;
    }
  };

  const total = items.reduce((sum, i) => sum + i.price * i.qty, 0);
  const count = items.reduce((sum, i) => sum + i.qty, 0);

  return (
    <CartContext.Provider value={{ items, addItem, removeItem, updateQty, clearCart, total, count, loading, setAuthCheck }}>
      {children}
    </CartContext.Provider>
  );
}
