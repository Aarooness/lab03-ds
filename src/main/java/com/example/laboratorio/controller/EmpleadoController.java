package com.example.laboratorio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.laboratorio.dto.ApiResponse;
import com.example.laboratorio.dto.EmpleadoRequest;
import com.example.laboratorio.dto.EmpleadoResponse;
import com.example.laboratorio.dto.EmpleadoUpdateRequest;
import com.example.laboratorio.service.EmpleadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PostMapping("/empleados")
    public ResponseEntity<ApiResponse<EmpleadoResponse>> crear(@Valid @RequestBody EmpleadoRequest request) {
        EmpleadoResponse empleado = empleadoService.crear(request);
        return ResponseEntity.status(201).body(ApiResponse.success("Empleado creado correctamente", empleado));
    }

    @GetMapping("/empleados")
    public ResponseEntity<ApiResponse<List<EmpleadoResponse>>> listarTodos() {
        List<EmpleadoResponse> empleados = empleadoService.listarTodos();
        return ResponseEntity.ok(ApiResponse.success("Lista de empleados obtenida", empleados));
    }

    @GetMapping("/empleados/{id}")
    public ResponseEntity<ApiResponse<EmpleadoResponse>> obtenerPorId(@PathVariable Long id) {
        EmpleadoResponse empleado = empleadoService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Empleado encontrado", empleado));
    }

    @PutMapping("/empleados/{id}")
    public ResponseEntity<ApiResponse<EmpleadoResponse>> actualizar(
        @PathVariable Long id,
        @Valid @RequestBody EmpleadoUpdateRequest request) {
        EmpleadoResponse empleadoActualizado = empleadoService.actualizar(id, request);
        return ResponseEntity.ok(ApiResponse.success("Empleado actualizado correctamente", empleadoActualizado));
    }

    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Empleado eliminado correctamente", null));
    }
}
