package com.vitor.library_api.service;

import com.vitor.library_api.model.Livro;
import com.vitor.library_api.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService (LivroRepository livroRepository){
        this.livroRepository = livroRepository;
    }

    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }

    public Livro cadastrarLivro(Livro livro) {
        return livroRepository.save(livro);
    }


}
