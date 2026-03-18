package com.app.ventas_api.Productos.IService;

import java.util.List;

import com.app.ventas_api.Productos.DTO.Request.LaboratoryRequestDto;
import com.app.ventas_api.Productos.DTO.Response.LaboratoryResponseDto;

public interface ILaboratoryService {
    
    List<LaboratoryResponseDto> findAll();
    
    List<LaboratoryResponseDto> findActive();
    
    LaboratoryResponseDto findById(Long id);
    
    LaboratoryResponseDto create(LaboratoryRequestDto request);
    
    LaboratoryResponseDto update(Long id, LaboratoryRequestDto request);
    
    void delete(Long id);
    
    boolean existsByName(String name);
}
