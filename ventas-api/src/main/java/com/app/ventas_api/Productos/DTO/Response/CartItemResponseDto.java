package com.app.ventas_api.Productos.DTO.Response;

public class CartItemResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productCategory;
    private String productLaboratory;
    private Double price;
    private Integer stock;
    private Integer quantity;
    private String imageUrl;

    public CartItemResponseDto() {}

    public CartItemResponseDto(Long id, Long productId, String productName, String productCategory, 
                               String productLaboratory, Double price, Integer stock, Integer quantity, String imageUrl) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productLaboratory = productLaboratory;
        this.price = price;
        this.stock = stock;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductLaboratory() {
        return productLaboratory;
    }

    public void setProductLaboratory(String productLaboratory) {
        this.productLaboratory = productLaboratory;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
