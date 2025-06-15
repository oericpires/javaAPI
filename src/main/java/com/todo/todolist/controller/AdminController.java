package com.todo.todolist.controller;

import com.todo.todolist.model.Usuario;
import com.todo.todolist.model.enuns.Cargo;
import com.todo.todolist.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('SCOPE_ADMINISTRADOR')")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping("/usuarios")
    public ResponseEntity<?> listarUsuarios() {
        try {
            return ResponseEntity.ok(usuarioRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao listar usuários: " + e.getMessage());
        }
    }

    @PutMapping("/usuarios/{id}/cargo")
    public ResponseEntity<String> updateCargo(@PathVariable UUID id, @RequestParam Cargo cargo) {
        var usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuário não encontrado!");
        }
        var usuario = usuarioOpt.get();
        usuario.setCargo(cargo);
        usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Usuário atualizado com sucesso!");
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable UUID id) {
        try {
            var usuario = usuarioRepository.findById(id);
            if (usuario.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuário não encontrado!");
            }
            usuarioRepository.delete(usuario.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Usuário deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao deletar usuário: " + e.getMessage());
        }
    }
}