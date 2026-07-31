package com.vitor.library_api.controller;

import com.vitor.library_api.model.Livro;
import org.springframework.web.bind.annotation.*;
import com.vitor.library_api.service.LivroService;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public List<Livro> listarLivros() {
        return livroService.listarLivros();
    }

    @PostMapping
    public Livro cadastrarLivro(@RequestBody Livro livro) {
       return livroService.cadastrarLivro(livro);
    }
}
