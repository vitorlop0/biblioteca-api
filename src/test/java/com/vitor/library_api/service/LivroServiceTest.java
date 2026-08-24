package com.vitor.library_api.service;

import com.vitor.library_api.dto.LivroRequest;
import com.vitor.library_api.dto.LivroResponse;
import com.vitor.library_api.exceptions.LivroNotFoundException;
import com.vitor.library_api.model.Livro;
import com.vitor.library_api.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LivroServiceTest {

    private LivroRepository livroRepository;
    private LivroService livroService;

    @BeforeEach
    public void setUp() {
        livroRepository = mock(LivroRepository.class);
        livroService = new LivroService(livroRepository);
    }

    @Test
    void deveBuscarLivroPorId() {
        Livro livroFake = new Livro();
        livroFake.setId(1L);
        livroFake.setTitulo("Dom Casmurro");
        livroFake.setAutor("Machado de Assis");

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livroFake));

        LivroResponse resultado = livroService.buscarLivroPorId(1L);

        assertAll(
                () -> assertEquals("Dom Casmurro", resultado.titulo()),
                () -> assertEquals("Machado de Assis", resultado.autor()),
                () -> assertEquals(1L, resultado.id())
        );
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoExiste() {
        when(livroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LivroNotFoundException.class, () -> livroService.buscarLivroPorId(1L));


    }

    @Test
    void deveCadastrarLivro() {
        LivroRequest request = new LivroRequest("Dom Casmurro", "Machado de Assis", 1899);

        Livro livroSalvo = new Livro();
        livroSalvo.setId(1L);
        livroSalvo.setTitulo("Dom Casmurro");
        livroSalvo.setAutor("Machado de Assis");
        livroSalvo.setAnoPublicacao(1899);

        when(livroRepository.save(any(Livro.class))).thenReturn(livroSalvo);

        LivroResponse resultado = livroService.cadastrarLivro(request);

        assertAll(
                () -> assertEquals("Dom Casmurro", resultado.titulo()),
                () -> assertEquals("Machado de Assis", resultado.autor()),
                () -> assertEquals(1L, resultado.id())
        );

    }

}
