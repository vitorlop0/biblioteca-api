package com.vitor.library_api.controller;

import com.vitor.library_api.dto.LivroRequest;
import com.vitor.library_api.dto.LivroResponse;
import com.vitor.library_api.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public List<LivroResponse> listarLivros() {
        return livroService.listarLivros();
    }

    @GetMapping("/{id}")
    public LivroResponse listarLivrosPorId(@PathVariable Long id) {
        return livroService.buscarLivroPorId(id);
    }

    @PostMapping
    public ResponseEntity<LivroResponse> cadastrarLivro(@Valid @RequestBody LivroRequest livro) {
        return ResponseEntity.ok(livroService.cadastrarLivro(livro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivroPorId(@PathVariable Long id) {
        livroService.deletarLivro(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponse> atualizarLivroPorId(@Valid @RequestBody LivroRequest livro, @PathVariable Long id) {
        return ResponseEntity.ok(livroService.atualizarLivroPorId(livro, id));
    }


}
