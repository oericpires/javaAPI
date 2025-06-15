package com.todo.todolist.controller;

import com.todo.todolist.repository.CarroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CarroController {

    private final CarroRepository carroRepository;

    @GetMapping("/carros")
    public ResponseEntity<?> listarCarros() {
        try {
            return ResponseEntity.ok(carroRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao listar os carros: " + e.getMessage());
        }
    }

    @GetMapping("/carros/{id}")
    public ResponseEntity<?> buscarCarroPorId(@PathVariable Long id) {
        try {
            var carro = carroRepository.findById(id);
            if (carro.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Carro não encontrado!");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(carro.get());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar carro: " + e.getMessage());
        }
    }
}
