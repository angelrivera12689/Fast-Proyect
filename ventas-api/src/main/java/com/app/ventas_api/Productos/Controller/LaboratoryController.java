package com.app.ventas_api.Productos.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.app.ventas_api.Productos.DTO.Request.LaboratoryRequestDto;
import com.app.ventas_api.Productos.DTO.Response.LaboratoryResponseDto;
import com.app.ventas_api.Productos.IService.ILaboratoryService;
import com.app.ventas_api.config.response.ApiResponse;

@RestController
@RequestMapping("/api/laboratories")
public class LaboratoryController {

    @Autowired
    private ILaboratoryService laboratoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LaboratoryResponseDto>>> getAll() {
        List<LaboratoryResponseDto> laboratories = laboratoryService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorios obtenidos exitosamente", laboratories));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<LaboratoryResponseDto>>> getActive() {
        List<LaboratoryResponseDto> laboratories = laboratoryService.findActive();
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorios activos obtenidos exitosamente", laboratories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LaboratoryResponseDto>> getById(@PathVariable Long id) {
        LaboratoryResponseDto laboratory = laboratoryService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorio obtenido exitosamente", laboratory));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LaboratoryResponseDto>> create(@Valid @RequestBody LaboratoryRequestDto request) {
        LaboratoryResponseDto laboratory = laboratoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Laboratorio creado exitosamente", laboratory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LaboratoryResponseDto>> update(@PathVariable Long id, @Valid @RequestBody LaboratoryRequestDto request) {
        LaboratoryResponseDto laboratory = laboratoryService.update(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorio actualizado exitosamente", laboratory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        laboratoryService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Laboratorio eliminado exitosamente", null));
    }

    @GetMapping("/exists/{name}")
    public ResponseEntity<ApiResponse<Boolean>> existsByName(@PathVariable String name) {
        boolean exists = laboratoryService.existsByName(name);
        return ResponseEntity.ok(new ApiResponse<>(exists, exists ? "El laboratorio existe" : "El laboratorio no existe", exists));
    }
}
