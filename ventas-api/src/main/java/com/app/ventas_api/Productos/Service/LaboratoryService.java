package com.app.ventas_api.Productos.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.ventas_api.Productos.DTO.Request.LaboratoryRequestDto;
import com.app.ventas_api.Productos.DTO.Response.LaboratoryResponseDto;
import com.app.ventas_api.Productos.Entity.Laboratory;
import com.app.ventas_api.Productos.IRepository.ILaboratoryRepository;
import com.app.ventas_api.Productos.IService.ILaboratoryService;
import com.app.ventas_api.config.exception.ResourceNotFoundException;

@Service
public class LaboratoryService implements ILaboratoryService {

    @Autowired
    private ILaboratoryRepository laboratoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LaboratoryResponseDto> findAll() {
        return laboratoryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaboratoryResponseDto> findActive() {
        return laboratoryRepository.findByActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LaboratoryResponseDto findById(Long id) {
        Laboratory laboratory = laboratoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laboratorio no encontrado con ID: " + id));
        return mapToDto(laboratory);
    }

    @Override
    @Transactional
    public LaboratoryResponseDto create(LaboratoryRequestDto request) {
        if (laboratoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Ya existe un laboratorio con el nombre: " + request.getName());
        }
        
        Laboratory laboratory = new Laboratory();
        laboratory.setName(request.getName());
        laboratory.setDescription(request.getDescription());
        laboratory.setCountry(request.getCountry());
        laboratory.setContactEmail(request.getContactEmail());
        laboratory.setContactPhone(request.getContactPhone());
        laboratory.setActive(request.getActive() != null ? request.getActive() : true);
        
        Laboratory saved = laboratoryRepository.save(laboratory);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public LaboratoryResponseDto update(Long id, LaboratoryRequestDto request) {
        Laboratory laboratory = laboratoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laboratorio no encontrado con ID: " + id));
        
        // Check if name is being changed and if it already exists
        if (request.getName() != null && !request.getName().equalsIgnoreCase(laboratory.getName())) {
            if (laboratoryRepository.existsByNameIgnoreCase(request.getName())) {
                throw new RuntimeException("Ya existe un laboratorio con el nombre: " + request.getName());
            }
            laboratory.setName(request.getName());
        }
        
        if (request.getDescription() != null) {
            laboratory.setDescription(request.getDescription());
        }
        if (request.getCountry() != null) {
            laboratory.setCountry(request.getCountry());
        }
        if (request.getContactEmail() != null) {
            laboratory.setContactEmail(request.getContactEmail());
        }
        if (request.getContactPhone() != null) {
            laboratory.setContactPhone(request.getContactPhone());
        }
        if (request.getActive() != null) {
            laboratory.setActive(request.getActive());
        }
        
        Laboratory updated = laboratoryRepository.save(laboratory);
        return mapToDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Laboratory laboratory = laboratoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laboratorio no encontrado con ID: " + id));
        laboratoryRepository.delete(laboratory);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return laboratoryRepository.existsByNameIgnoreCase(name);
    }

    private LaboratoryResponseDto mapToDto(Laboratory laboratory) {
        LaboratoryResponseDto dto = new LaboratoryResponseDto();
        dto.setId(laboratory.getId());
        dto.setName(laboratory.getName());
        dto.setDescription(laboratory.getDescription());
        dto.setCountry(laboratory.getCountry());
        dto.setContactEmail(laboratory.getContactEmail());
        dto.setContactPhone(laboratory.getContactPhone());
        dto.setActive(laboratory.getActive());
        dto.setCreatedAt(laboratory.getCreatedAt());
        dto.setUpdatedAt(laboratory.getUpdatedAt());
        return dto;
    }
}
