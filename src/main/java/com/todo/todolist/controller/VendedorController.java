package com.todo.todolist.controller;

import com.todo.todolist.controller.DTO.CarroDTO.CadastroCarroDTO;
import com.todo.todolist.model.Carro;
import com.todo.todolist.repository.CarroRepository;
import com.todo.todolist.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/vendedor")
@PreAuthorize("hasAuthority('SCOPE_VENDEDOR')")
@RequiredArgsConstructor
public class VendedorController {

    private final CarroRepository CarroRepository;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/carros")
    public ResponseEntity<String> cadastrarCarro(@RequestBody CadastroCarroDTO dto, JwtAuthenticationToken token) {
        try {
            if (dto == null || dto.marca() == null || dto.modelo() == null || dto.ano() <= 0 || dto.preco() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Todos os campos são obrigatórios e devem ser válidos.");
            }
            UUID id = UUID.fromString(token.getName());
            Carro carro = new Carro();
            carro.setMarca(dto.marca());
            carro.setModelo(dto.modelo());
            carro.setAno(dto.ano());
            carro.setPreco(dto.preco());
            carro.setDescricao(dto.descricao());
            var usuario = usuarioRepository.findById(id);
            if (usuario.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuário não encontrado!");
            }
            carro.setUsuario(usuario.get());
            CarroRepository.save(carro);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Carro cadastrado com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao cadastrar carro." + e.getMessage());
        }
    }

    @PutMapping("/carros/{id}")
    public ResponseEntity<String> atualizarCarro(@PathVariable long id, @RequestBody CadastroCarroDTO dto) {
        try {
            var carroOpt = CarroRepository.findById(id);
            if (carroOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Carro não encontrado!");
            }
            Carro carro = carroOpt.get();
            if (dto.marca() != null) carro.setMarca(dto.marca());
            if (dto.modelo() != null) carro.setModelo(dto.modelo());
            if (dto.ano() > 0) carro.setAno(dto.ano());
            if (dto.preco() > 0) carro.setPreco(dto.preco());
            if (dto.descricao() != null) carro.setDescricao(dto.descricao());

            CarroRepository.save(carro);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Carro atualizado com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar carro: " + e.getMessage());
        }
    }

    @DeleteMapping("/carros/{id}")
    public ResponseEntity<String> deletarCarro(@PathVariable long id) {
        try {
            var carroOpt = CarroRepository.findById(id);
            if (carroOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Carro não encontrado!");
            }
            CarroRepository.delete(carroOpt.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Carro deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao deletar carro: " + e.getMessage());
        }
    }
}
