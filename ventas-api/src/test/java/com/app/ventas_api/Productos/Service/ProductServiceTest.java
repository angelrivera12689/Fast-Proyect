package com.app.ventas_api.Productos.Service;

import com.app.ventas_api.Productos.Entity.Category;
import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.Productos.IRepository.IProductRepository;
import com.app.ventas_api.Productos.IService.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests Unitarios para ProductService
 * 
 * Patrón: Arrange-Act-Assert
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(1L)
                .name("Medicamentos")
                .description("Categoría de medicamentos")
                .active(true)
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Acetaminofén 500mg")
                .description("Analgésico y antipirético")
                .basePrice(new BigDecimal("5000.00"))
                .stock(100)
                .sku("MED-001")
                .category(testCategory)
                .active(true)
                .build();
    }

    // ===== Tests de Lectura =====

    @Test
    void testFindAll_ReturnsAllProducts() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(
                testProduct,
                Product.builder().id(2L).name("Ibuprofeno").basePrice(new BigDecimal("8000")).stock(50).build()
        );
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.all();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById_WhenExists_ReturnsProduct() throws Exception {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        Optional<Product> result = productService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Acetaminofén 500mg", result.get().getName());
    }

    @Test
    void testFindById_WhenNotExists_ThrowsException() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productService.findById(999L);
        });
        assertTrue(exception.getMessage().contains("Product not found"));
    }

    @Test
    void testFindByStateTrue_ReturnsOnlyActiveProducts() throws Exception {
        // Arrange
        List<Product> activeProducts = Arrays.asList(testProduct);
        when(productRepository.findByActive(true)).thenReturn(activeProducts);

        // Act
        List<Product> result = productService.findByStateTrue();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
    }

    // ===== Tests de Escritura =====

    @Test
    void testSave_NewProduct_ReturnsSavedProduct() throws Exception {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product result = productService.save(testProduct);

        // Assert
        assertNotNull(result);
        assertEquals(testProduct.getName(), result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdate_ExistingProduct_UpdatesSuccessfully() throws Exception {
        // Arrange
        Product updatedProduct = Product.builder()
                .id(1L)
                .name("Acetaminofén 1000mg")
                .description("Nueva descripción")
                .basePrice(new BigDecimal("8000.00"))
                .stock(50)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        productService.update(1L, updatedProduct);

        // Assert
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDelete_ExistingProduct_DeletesSuccessfully() throws Exception {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).delete(testProduct);

        // Act
        productService.delete(1L);

        // Assert
        verify(productRepository, times(1)).delete(testProduct);
    }

    // ===== Tests de Negocio =====

    @Test
    void testFindByCategoryId_ReturnsProductsOfCategory() {
        // Arrange
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByCategory_Id(1L)).thenReturn(products);

        // Act
        List<Product> result = productService.findByCategoryId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCategory().getId());
    }

    @Test
    void testFindBySku_WhenExists_ReturnsProduct() throws Exception {
        // Arrange
        when(productRepository.findBySku("MED-001")).thenReturn(Optional.of(testProduct));

        // Act
        Optional<Product> result = productService.findBySku("MED-001");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("MED-001", result.get().getSku());
    }

    @Test
    void testExistsBySku_ReturnsTrue() {
        // Arrange
        when(productRepository.existsBySku("MED-001")).thenReturn(true);

        // Act
        boolean result = productService.existsBySku("MED-001");

        // Assert
        assertTrue(result);
    }

    @Test
    void testExistsBySku_ReturnsFalse() {
        // Arrange
        when(productRepository.existsBySku("NON-EXISTENT")).thenReturn(false);

        // Act
        boolean result = productService.existsBySku("NON-EXISTENT");

        // Assert
        assertFalse(result);
    }
}
