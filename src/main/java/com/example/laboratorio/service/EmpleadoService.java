package com.example.laboratorio.service;

import java.util.List;

import com.example.laboratorio.dto.AuthResponse;
import com.example.laboratorio.dto.EmpleadoRequest;
import com.example.laboratorio.dto.EmpleadoResponse;
import com.example.laboratorio.dto.EmpleadoUpdateRequest;
import com.example.laboratorio.dto.LoginRequest;

public interface EmpleadoService {

    EmpleadoResponse crear(EmpleadoRequest request);

    List<EmpleadoResponse> listarTodos();

    EmpleadoResponse obtenerPorId(Long id);

    EmpleadoResponse actualizar(Long id, EmpleadoUpdateRequest request);

    void eliminar(Long id);

    AuthResponse autenticar(LoginRequest request);
}
