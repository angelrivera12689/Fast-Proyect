package com.app.ventas_api.Organizacion.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CompanyAddressRequestDto {

    @NotBlank(message = "La etiqueta es requerida")
    @Size(min = 2, max = 100, message = "La etiqueta debe tener entre 2 y 100 caracteres")
    private String label;

    @NotBlank(message = "La dirección es requerida")
    @Size(min = 5, max = 500, message = "La dirección debe tener entre 5 y 500 caracteres")
    private String address;

    @NotBlank(message = "La ciudad es requerida")
    @Size(min = 2, max = 100, message = "La ciudad debe tener entre 2 y 100 caracteres")
    private String city;

    @Size(min = 2, max = 100, message = "El departamento debe tener entre 2 y 100 caracteres")
    private String department;

    @Size(min = 3, max = 20, message = "El código postal debe tener entre 3 y 20 caracteres")
    private String zipCode;

    @NotNull(message = "La latitud es requerida")
    private Double latitude;

    @NotNull(message = "La longitud es requerida")
    private Double longitude;

    @Size(min = 2, max = 150, message = "El nombre de contacto debe tener entre 2 y 150 caracteres")
    private String contactName;

    @Size(min = 7, max = 20, message = "El teléfono de contacto debe tener entre 7 y 20 caracteres")
    private String contactPhone;

    private Boolean isDefault;

    // Getters and Setters
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}
