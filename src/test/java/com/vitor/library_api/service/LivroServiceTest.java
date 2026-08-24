package com.vitor.library_api.service;

import com.vitor.library_api.dto.LivroRequest;
import com.vitor.library_api.dto.LivroResponse;
import com.vitor.library_api.exceptions.LivroNotFoundException;
import com.vitor.library_api.model.Livro;
import com.vitor.library_api.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

        ArgumentCaptor<Livro> captor = ArgumentCaptor.forClass(Livro.class);
        verify(livroRepository).save(captor.capture());
        Livro livroEnviado = captor.getValue();

        assertAll(
                () -> assertEquals("Dom Casmurro", resultado.titulo()),
                () -> assertEquals("Machado de Assis", resultado.autor()),
                () -> assertEquals(1L, resultado.id()),
                () -> assertEquals("Dom Casmurro", livroEnviado.getTitulo()),
                () -> assertEquals("Machado de Assis", livroEnviado.getAutor()),
                () -> assertEquals(1899, livroEnviado.getAnoPublicacao())
        );

    }

    @Test
    void deveListarLivros() {
        Livro livro1 = new Livro();
        livro1.setId(1L);
        livro1.setTitulo("Dom Casmurro");
        livro1.setAutor("Machado de Assis");

        Livro livro2 = new Livro();
        livro2.setId(2L);
        livro2.setTitulo("O Cortiço");
        livro2.setAutor("Aluísio Azevedo");

        when(livroRepository.findAll()).thenReturn(List.of(livro1, livro2));

        List<LivroResponse> resultado = livroService.listarLivros();

        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals("Dom Casmurro", resultado.get(0).titulo()),
                () -> assertEquals("O Cortiço", resultado.get(1).titulo())
        );
    }

    @Test
    void deveAtualizarLivroPorId() {
        LivroRequest request = new LivroRequest("Novo Titulo", "Novo Autor", 2020);

        Livro livroExistente = new Livro();
        livroExistente.setId(1L);
        livroExistente.setTitulo("Dom Casmurro");
        livroExistente.setAutor("Machado de Assis");
        livroExistente.setAnoPublicacao(1899);

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livroExistente));
        when(livroRepository.save(any(Livro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LivroResponse resultado = livroService.atualizarLivroPorId(request, 1L);

        assertAll(
                () -> assertEquals("Novo Titulo", resultado.titulo()),
                () -> assertEquals("Novo Autor", resultado.autor()),
                () -> assertEquals(2020, resultado.anoPublicacao()),
                () -> assertEquals(1L, resultado.id())
        );
    }

    @Test
    void deveLancarExcecaoAoAtualizarLivroInexistente() {
        LivroRequest request = new LivroRequest("Novo Titulo", "Novo Autor", 2020);

        when(livroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LivroNotFoundException.class, () -> livroService.atualizarLivroPorId(request, 1L));

        verify(livroRepository, never()).save(any(Livro.class));
    }

    @Test
    void deveDeletarLivro() {
        when(livroRepository.existsById(1L)).thenReturn(true);

        livroService.deletarLivro(1L);

        verify(livroRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarLivroInexistente() {
        when(livroRepository.existsById(1L)).thenReturn(false);

        assertThrows(LivroNotFoundException.class, () -> livroService.deletarLivro(1L));

        verify(livroRepository, never()).deleteById(any());
    }

}
