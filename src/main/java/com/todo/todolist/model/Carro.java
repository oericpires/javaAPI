package com.todo.todolist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "carros")
@Data @AllArgsConstructor @NoArgsConstructor
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private long id;

    @Column(name = "marca", nullable = false)
    private String marca;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "ano", nullable = false)
    private int ano;

    @Column(name = "preco", nullable = false)
    private double preco;

    @Column(name = "disponivel", nullable = false)
    private boolean disponivel = true;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "criado_em", updatable = false, nullable = false)
    private LocalDateTime criado_em;

    @PrePersist
    public void setCreatedAt() {
        this.criado_em = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
