package com.vitor.library_api.service;

import com.vitor.library_api.dto.LivroRequest;
import com.vitor.library_api.dto.LivroResponse;
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

    public List<LivroResponse> listarLivros() {
        return livroRepository.findAll()
                .stream()
                .map(livro -> new LivroResponse(
                        livro.getId(),
                        livro.getTitulo(),
                        livro.getAutor(),
                        livro.getAnoPublicacao(),
                        livro.getDisponivel()
                ))
                .toList();
    }

    public LivroResponse buscarLivroPorId(Long id) {
         Livro livro = livroRepository.findById(id)
                .orElseThrow(() ->
                        new LivroNotFoundException("Livro não encontrado"));
         return new LivroResponse(
                 livro.getId(),
                 livro.getTitulo(),
                 livro.getAutor(),
                 livro.getAnoPublicacao(),
                 livro.getDisponivel()
         );
    }

    public LivroResponse cadastrarLivro(LivroRequest livro) {
        Livro novoLivro = new Livro();
        novoLivro.setAutor(livro.autor());
        novoLivro.setTitulo(livro.titulo());
        novoLivro.setAnoPublicacao(livro.anoPublicacao());
        Livro livroSalvo = livroRepository.save(novoLivro);

        return new LivroResponse(
                livroSalvo.getId(),
                livroSalvo.getTitulo(),
                livroSalvo.getAutor(),
                livroSalvo.getAnoPublicacao(),
                livroSalvo.getDisponivel()
        );
    }

    public void deletarLivro(Long id) {
       if (livroRepository.existsById(id)) {
           livroRepository.deleteById(id);
        } else {
           throw new LivroNotFoundException("Livro não encontrado");
       }
    }

    public LivroResponse atualizarLivroPorId(LivroRequest livro, Long id) {
        Livro livroExistente = livroRepository.findById(id)
               .orElseThrow(() -> new LivroNotFoundException("Livro não encontrado"));

        livroExistente.setTitulo(livro.titulo());
        livroExistente.setAutor(livro.autor());
        livroExistente.setAnoPublicacao(livro.anoPublicacao());
        Livro livroSalvo = livroRepository.save(livroExistente);

        return new LivroResponse(
                livroSalvo.getId(),
                livroSalvo.getTitulo(),
                livroSalvo.getAutor(),
                livroSalvo.getAnoPublicacao(),
                livroSalvo.getDisponivel()
        );
    }


}
