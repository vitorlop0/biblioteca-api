package com.vitor.library_api.service;

import com.vitor.library_api.exceptions.LivroNotFoundException;
import com.vitor.library_api.model.Livro;
import com.vitor.library_api.repository.LivroRepository;
import org.springframework.stereotype.Service;

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

    public Livro buscarLivroPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() ->
                        new LivroNotFoundException("Livro não encontrado"));
    }

    public Livro cadastrarLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    public void deletarLivro(Long id) {
       if (livroRepository.existsById(id)) {
           livroRepository.deleteById(id);
        } else {
           throw new LivroNotFoundException("Livro não encontrado");
       }
    }


}
