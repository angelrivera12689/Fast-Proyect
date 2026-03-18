import { API_BASE_URL } from './apiConstants';
import { getAuthToken } from './auth';

const PRODUCTS_ENDPOINT = `${API_BASE_URL}/api/products`;
const CATEGORIES_ENDPOINT = `${API_BASE_URL}/api/categories`;
const LABORATORIES_ENDPOINT = `${API_BASE_URL}/api/laboratories`;

// Headers con autenticación
const getAuthHeaders = () => {
  const token = getAuthToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
};

/**
 * Obtiene todos los productos activos
 */
export const getProducts = async () => {
  try {
    const response = await fetch(`${PRODUCTS_ENDPOINT}/active`, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al obtener productos');
    }

    const products = await response.json();
    return transformProducts(products);
  } catch (error) {
    console.error('Error fetching products:', error);
    throw error;
  }
};

/**
 * Obtiene todos los productos (incluir inactivos para admin)
 */
export const getAllProducts = async () => {
  try {
    const response = await fetch(PRODUCTS_ENDPOINT, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al obtener productos');
    }

    const products = await response.json();
    return transformProducts(products);
  } catch (error) {
    console.error('Error fetching all products:', error);
    throw error;
  }
};

/**
 * Crea un nuevo producto
 */
export const createProduct = async (productData) => {
  try {
    const response = await fetch(PRODUCTS_ENDPOINT, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
      body: JSON.stringify(productData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al crear producto');
    }

    const product = await response.json();
    return transformProduct(product);
  } catch (error) {
    console.error('Error creating product:', error);
    throw error;
  }
};

/**
 * Actualiza un producto existente
 */
export const updateProduct = async (id, productData) => {
  try {
    const response = await fetch(`${PRODUCTS_ENDPOINT}/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
      body: JSON.stringify(productData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al actualizar producto');
    }

    const product = await response.json();
    return transformProduct(product);
  } catch (error) {
    console.error('Error updating product:', error);
    throw error;
  }
};

/**
 * Elimina un producto
 */
export const deleteProduct = async (id) => {
  try {
    const response = await fetch(`${PRODUCTS_ENDPOINT}/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al eliminar producto');
    }

    return true;
  } catch (error) {
    console.error('Error deleting product:', error);
    throw error;
  }
};

/**
 * Obtiene un producto por ID
 */
export const getProductById = async (id) => {
  try {
    const response = await fetch(`${PRODUCTS_ENDPOINT}/${id}`, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Producto no encontrado');
    }

    const product = await response.json();
    return transformProduct(product);
  } catch (error) {
    console.error('Error fetching product:', error);
    throw error;
  }
};

/**
 * Obtiene productos por categoría
 */
export const getProductsByCategory = async (categoryId) => {
  try {
    const response = await fetch(`${PRODUCTS_ENDPOINT}/category/${categoryId}`, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al obtener productos por categoría');
    }

    const products = await response.json();
    return transformProducts(products);
  } catch (error) {
    console.error('Error fetching products by category:', error);
    throw error;
  }
};

/**
 * Busca productos por nombre
 */
export const searchProducts = async (name) => {
  try {
    const response = await fetch(`${PRODUCTS_ENDPOINT}/search?name=${encodeURIComponent(name)}`, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al buscar productos');
    }

    const products = await response.json();
    return transformProducts(products);
  } catch (error) {
    console.error('Error searching products:', error);
    throw error;
  }
};

/**
 * Obtiene todas las categorías
 */
export const getCategories = async () => {
  try {
    const response = await fetch(CATEGORIES_ENDPOINT, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al obtener categorías');
    }

    return await response.json();
  } catch (error) {
    console.error('Error fetching categories:', error);
    throw error;
  }
};

/**
 * Obtiene categorías activas
 */
export const getActiveCategories = async () => {
  try {
    const response = await fetch(`${CATEGORIES_ENDPOINT}/active`, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al obtener categorías activas');
    }

    return await response.json();
  } catch (error) {
    console.error('Error fetching active categories:', error);
    throw error;
  }
};

/**
 * Crea una nueva categoría
 */
export const createCategory = async (categoryData) => {
  try {
    const response = await fetch(CATEGORIES_ENDPOINT, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
      body: JSON.stringify(categoryData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al crear categoría');
    }

    return await response.json();
  } catch (error) {
    console.error('Error creating category:', error);
    throw error;
  }
};

/**
 * Actualiza una categoría existente
 */
export const updateCategory = async (id, categoryData) => {
  try {
    const response = await fetch(`${CATEGORIES_ENDPOINT}/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
      body: JSON.stringify(categoryData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al actualizar categoría');
    }

    return await response.json();
  } catch (error) {
    console.error('Error updating category:', error);
    throw error;
  }
};

/**
 * Elimina una categoría
 */
export const deleteCategory = async (id) => {
  try {
    const response = await fetch(`${CATEGORIES_ENDPOINT}/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al eliminar categoría');
    }

    return true;
  } catch (error) {
    console.error('Error deleting category:', error);
    throw error;
  }
};

/**
 * Obtiene todos los laboratorios
 */
export const getLaboratories = async () => {
  try {
    const response = await fetch(LABORATORIES_ENDPOINT, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al obtener laboratorios');
    }

    const result = await response.json();
    return result.data || [];
  } catch (error) {
    console.error('Error fetching laboratories:', error);
    throw error;
  }
};

/**
 * Obtiene laboratorios activos
 */
export const getActiveLaboratories = async () => {
  try {
    const response = await fetch(`${LABORATORIES_ENDPOINT}/active`, {
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      throw new Error('Error al obtener laboratorios activos');
    }

    const result = await response.json();
    return result.data || [];
  } catch (error) {
    console.error('Error fetching active laboratories:', error);
    throw error;
  }
};

/**
 * Crea un nuevo laboratorio
 */
export const createLaboratory = async (laboratoryData) => {
  try {
    const response = await fetch(LABORATORIES_ENDPOINT, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
      body: JSON.stringify(laboratoryData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al crear laboratorio');
    }

    const result = await response.json();
    return result.data;
  } catch (error) {
    console.error('Error creating laboratory:', error);
    throw error;
  }
};

/**
 * Actualiza un laboratorio existente
 */
export const updateLaboratory = async (id, laboratoryData) => {
  try {
    const response = await fetch(`${LABORATORIES_ENDPOINT}/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
      body: JSON.stringify(laboratoryData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al actualizar laboratorio');
    }

    const result = await response.json();
    return result.data;
  } catch (error) {
    console.error('Error updating laboratory:', error);
    throw error;
  }
};

/**
 * Elimina un laboratorio
 */
export const deleteLaboratory = async (id) => {
  try {
    const response = await fetch(`${LABORATORIES_ENDPOINT}/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeaders(),
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Error al eliminar laboratorio');
    }

    return true;
  } catch (error) {
    console.error('Error deleting laboratory:', error);
    throw error;
  }
};

/**
 * Transforma los datos del backend al formato esperado por el frontend
 */
const transformProduct = (product) => {
  // Manejar category que puede venir como objeto {id, name} o estar ausente
  const categoryData = product.category;
  const categoryId = categoryData?.id || product.categoryId || null;
  const categoryName = categoryData?.name || product.categoryName || null;
  
  // Laboratory info
  const laboratoryData = product.laboratory;
  const laboratoryId = laboratoryData?.id || product.laboratoryId || null;
  const laboratoryName = laboratoryData?.name || product.laboratory || null;
  
  return {
    id: product.id,
    name: product.name,
    description: product.description || '',
    price: parseFloat(product.basePrice) || 0,
    stock: product.stock || 0,
    sku: product.sku || '',
    imageUrl: product.imageUrl || null,
    // Category info - solo valores primitivos
    categoryId: categoryId,
    category: categoryName,
    categoryName: categoryName,
    // Laboratory info - solo valores primitivos
    lab: laboratoryName || 'General',
    laboratory: laboratoryName,
    laboratoryId: laboratoryId,
    registrationNumber: product.registrationNumber,
    dosage: product.dosage,
    expirationDate: product.expirationDate || null,
    dimensions: product.dimensions,
    activeIngredient: product.activeIngredient,
    presentation: product.presentation,
    requiresPrescription: product.requiresPrescription || false,
    active: product.active,
    // Valor por defecto para minOrder
    minOrder: 1,
  };
};

/**
 * Transforma un array de productos
 */
const transformProducts = (products) => {
  if (!Array.isArray(products)) {
    return [];
  }
  return products.map(transformProduct);
};

export default {
  getProducts,
  getAllProducts,
  getProductById,
  getProductsByCategory,
  searchProducts,
  getCategories,
  getActiveCategories,
  createCategory,
  updateCategory,
  deleteCategory,
  createProduct,
  updateProduct,
  deleteProduct,
  // Laboratories
  getLaboratories,
  getActiveLaboratories,
  createLaboratory,
  updateLaboratory,
  deleteLaboratory,
};
