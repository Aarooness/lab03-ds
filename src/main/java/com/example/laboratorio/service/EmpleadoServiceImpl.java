package com.example.laboratorio.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.laboratorio.dto.AuthResponse;
import com.example.laboratorio.dto.EmpleadoRequest;
import com.example.laboratorio.dto.EmpleadoResponse;
import com.example.laboratorio.dto.EmpleadoUpdateRequest;
import com.example.laboratorio.dto.LoginRequest;
import com.example.laboratorio.entity.Empleado;
import com.example.laboratorio.exception.DuplicateEmailException;
import com.example.laboratorio.exception.InvalidCredentialsException;
import com.example.laboratorio.exception.ResourceNotFoundException;
import com.example.laboratorio.repository.EmpleadoRepository;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository, PasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public EmpleadoResponse crear(EmpleadoRequest request) {
        if (empleadoRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("El email ya existe en el sistema");
        }

        Empleado empleado = new Empleado();
        empleado.setNombre(request.nombre());
        empleado.setEmail(request.email().trim());
        empleado.setRol(request.rol().trim().toLowerCase());
        empleado.setCargo(request.cargo().trim());
        empleado.setPassword(passwordEncoder.encode(request.password()));

        Empleado guardado = empleadoRepository.save(empleado);
        return mapToResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponse> listarTodos() {
        return empleadoRepository.findAll().stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponse obtenerPorId(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
        return mapToResponse(empleado);
    }

    @Override
    @Transactional
    public EmpleadoResponse actualizar(Long id, EmpleadoUpdateRequest request) {
        Empleado empleado = empleadoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));

        if (request.email() != null && !request.email().isBlank() && !request.email().equalsIgnoreCase(empleado.getEmail())
            && empleadoRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("El email ya existe en el sistema");
        }

        if (request.nombre() != null && !request.nombre().isBlank()) {
            empleado.setNombre(request.nombre().trim());
        }
        if (request.email() != null && !request.email().isBlank()) {
            empleado.setEmail(request.email().trim());
        }
        if (request.rol() != null && !request.rol().isBlank()) {
            empleado.setRol(request.rol().trim().toLowerCase());
        }
        if (request.cargo() != null && !request.cargo().isBlank()) {
            empleado.setCargo(request.cargo().trim());
        }
        if (request.password() != null && !request.password().isBlank()) {
            empleado.setPassword(passwordEncoder.encode(request.password()));
        }

        Empleado actualizado = empleadoRepository.save(empleado);
        return mapToResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
        empleadoRepository.delete(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse autenticar(LoginRequest request) {
        Empleado empleado = empleadoRepository.findByEmail(request.email().trim())
            .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), empleado.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        String token = UUID.randomUUID().toString();
        return new AuthResponse(token, empleado.getEmail(), empleado.getRol());
    }

    private EmpleadoResponse mapToResponse(Empleado empleado) {
        return new EmpleadoResponse(
            empleado.getId(),
            empleado.getNombre(),
            empleado.getEmail(),
            empleado.getRol(),
            empleado.getCargo()
        );
    }
}
