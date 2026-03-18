import { API_BASE_URL } from './apiConstants';
import { getAuthToken } from './auth';

const WISHLIST_ENDPOINT = `${API_BASE_URL}/api/wishlist`;

// Headers con autenticación
const getAuthHeaders = () => {
  const token = getAuthToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
};

/**
 * Obtiene la wishlist del usuario autenticado
 * GET /api/wishlist
 */
export const getWishlist = async () => {
  try {
    const response = await fetch(WISHLIST_ENDPOINT, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al obtener wishlist');
    }

    const wishlist = await response.json();
    return transformWishlist(wishlist);
  } catch (error) {
    console.error('Error fetching wishlist:', error);
    throw error;
  }
};

/**
 * Agrega un producto a la wishlist
 * POST /api/wishlist/{productId}
 */
export const addToWishlist = async (productId) => {
  try {
    const response = await fetch(`${WISHLIST_ENDPOINT}/${productId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al agregar a wishlist');
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error('Error adding to wishlist:', error);
    throw error;
  }
};

/**
 * Elimina un producto de la wishlist
 * DELETE /api/wishlist/{productId}
 */
export const removeFromWishlist = async (productId) => {
  try {
    const response = await fetch(`${WISHLIST_ENDPOINT}/${productId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al eliminar de wishlist');
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error('Error removing from wishlist:', error);
    throw error;
  }
};

/**
 * Verifica si un producto está en la wishlist
 * GET /api/wishlist/check/{productId}
 */
export const checkInWishlist = async (productId) => {
  try {
    const response = await fetch(`${WISHLIST_ENDPOINT}/check/${productId}`, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al verificar wishlist');
    }

    const result = await response.json();
    return result.inWishlist || false;
  } catch (error) {
    console.error('Error checking wishlist:', error);
    return false;
  }
};

/**
 * Transforma los datos del backend al formato esperado por el frontend
 */
const transformWishlist = (wishlistItems) => {
  if (!Array.isArray(wishlistItems)) {
    return [];
  }

  return wishlistItems.map((item) => ({
    id: item.id,
    productId: item.product?.id || item.productId,
    product: item.product ? {
      id: item.product.id,
      name: item.product.name,
      description: item.product.description,
      price: parseFloat(item.product.basePrice) || 0,
      imageUrl: item.product.imageUrl || null,
      sku: item.product.sku || '',
      stock: item.product.stock || 0,
      category: item.product.category?.name || null,
      laboratory: item.product.laboratory?.name || null,
    } : null,
    addedAt: item.createdAt,
  }));
};

export default {
  getWishlist,
  addToWishlist,
  removeFromWishlist,
  checkInWishlist,
};
