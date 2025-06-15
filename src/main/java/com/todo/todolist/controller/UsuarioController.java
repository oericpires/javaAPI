package com.todo.todolist.controller;


import com.todo.todolist.controller.DTO.UsuarioDTO.LoginDTO;
import com.todo.todolist.controller.DTO.UsuarioDTO.RegistroDTO;
import com.todo.todolist.model.Usuario;
import com.todo.todolist.model.enuns.Cargo;
import com.todo.todolist.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;


    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@RequestBody LoginDTO dto) {
        if (dto.senha().trim() == null || dto.email().trim() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email e senha são obrigatórios!");
        }
        try {
            var user = usuarioRepository.findByEmail(dto.email());
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuário não encontrado!");
            }
            if (!passwordEncoder.matches(dto.senha(), user.get().getSenha())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("email ou senha inválidos!");
            }
            var now = Instant.now();
            var expiresIn = 3000L;

            var scopes = user.get().getCargo().name();

            var claims = JwtClaimsSet.builder()
                    .subject(user.get().getId().toString())
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiresIn))
                    .claim("scope", scopes)
                    .build();

            var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(jwtValue);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao fazer login: " + e.getMessage());
        }
    }


    @Transactional
    @PostMapping("/registrar")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody RegistroDTO dto) {
        try {
            var user = usuarioRepository.findByEmail(dto.email());
            if (user.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email já cadastrado!");
            }

            if (dto.nome() == null || dto.email() == null || dto.senha() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Todos os campos são obrigatórios!");
            }

            Usuario usuario = new Usuario();
            usuario.setNome(dto.nome());
            usuario.setEmail(dto.email());
            usuario.setSenha(passwordEncoder.encode(dto.senha()));

            usuarioRepository.save(usuario);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuário cadastrado com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/usuario")
    public ResponseEntity<?> usuarios(JwtAuthenticationToken token) {
        try {
            UUID id = UUID.fromString(token.getName());
            var usuario = usuarioRepository.findById(id);
            if (usuario.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuário não encontrado!");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(usuario.get());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro: " + e.getMessage());
        }
    }

    @Transactional
    @PutMapping("/usuario")
    public ResponseEntity<String> atualizarUsuario(@RequestBody RegistroDTO dto, JwtAuthenticationToken token) {
        try {
            UUID id = UUID.fromString(token.getName());
            var usuarioOpt = usuarioRepository.findById(id);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuário não encontrado!");
            }

            var existingUser = usuarioRepository.findByEmail(dto.email());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email já cadastrado!");
            }

            Usuario usuario = usuarioOpt.get();
            if (dto.nome() != null ) usuario.setNome(dto.nome());
            if (dto.email() != null ) usuario.setEmail(dto.email());
            if (dto.senha() != null ) usuario.setSenha(passwordEncoder.encode(dto.senha()));

            usuarioRepository.save(usuario);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Usuário atualizado com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/usuario")
    public ResponseEntity<String> deletarUsuario(JwtAuthenticationToken token) {
        try {
            UUID id = UUID.fromString(token.getName());
            var usuarioOpt = usuarioRepository.findById(id);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuário não encontrado!");
            }

            usuarioRepository.delete(usuarioOpt.get());

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Usuário deletado com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar usuário: " + e.getMessage());
        }
    }
}
