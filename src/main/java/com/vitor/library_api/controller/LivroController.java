package com.vitor.library_api.controller;

import com.vitor.library_api.dto.LivroRequest;
import com.vitor.library_api.dto.LivroResponse;
import com.vitor.library_api.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Livros", description = "Operações de CRUD para gerenciamento de livros")
@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @Operation(summary = "Lista todos os livros da API")
    @GetMapping
    public List<LivroResponse> listarLivros() {
        return livroService.listarLivros();
    }

    @Operation(summary = "Lista um livro pelo seu ID")
    @GetMapping("/{id}")
    public LivroResponse listarLivroPorId(@PathVariable Long id) {
        return livroService.buscarLivroPorId(id);
    }

    @Operation(summary = "Cadastra um livro")
    @PostMapping
    public ResponseEntity<LivroResponse> cadastrarLivro(@Valid @RequestBody LivroRequest livro) {
        return ResponseEntity.ok(livroService.cadastrarLivro(livro));
    }

    @Operation(summary = "Deleta um livro pelo seu ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivroPorId(@PathVariable Long id) {
        livroService.deletarLivro(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualiza um livro pelo ID")
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponse> atualizarLivroPorId(@Valid @RequestBody LivroRequest livro, @PathVariable Long id) {
        return ResponseEntity.ok(livroService.atualizarLivroPorId(livro, id));
    }


}
