package com.vitor.library_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitor.library_api.dto.LivroRequest;
import com.vitor.library_api.dto.LivroResponse;
import com.vitor.library_api.exceptions.LivroNotFoundException;
import com.vitor.library_api.exceptions.LivroOperacaoInvalidaException;
import com.vitor.library_api.service.LivroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LivroController.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private LivroService livroService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void listarLivros() throws Exception {
        LivroResponse livro = new LivroResponse(1L, "Dom Casmurro", "Machado de Assis", 1899, true);
        when(livroService.listarLivros()).thenReturn(List.of(livro));

        mockMvc.perform(get("/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Dom Casmurro"));
    }

    @Test
    void listarLivroPorId() throws Exception {
        LivroResponse livro = new LivroResponse(1L, "Dom Casmurro", "Machado de Assis", 1899, true);
        when(livroService.buscarLivroPorId(1L)).thenReturn(livro);

        mockMvc.perform(get("/livros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro"));
    }

    @Test
    void listarLivroPorId_NaoEncontrado() throws Exception {
        when(livroService.buscarLivroPorId(999L)).thenThrow(new LivroNotFoundException("Livro não encontrado"));

        mockMvc.perform(get("/livros/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Livro não encontrado"));
    }

    @Test
    void cadastrarLivro() throws Exception {
        LivroRequest request = new LivroRequest("Dom Casmurro", "Machado de Assis", 1899);
        LivroResponse response = new LivroResponse(1L, "Dom Casmurro", "Machado de Assis", 1899, true);

        when(livroService.cadastrarLivro(any(LivroRequest.class))).thenReturn(response);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro"));
    }

    @Test
    void cadastrarLivro_TituloEmBranco() throws Exception {
        LivroRequest request = new LivroRequest("", "Machado de Assis", 1899);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletarLivroPorId() throws Exception {
        doNothing().when(livroService).deletarLivro(1L);

        mockMvc.perform(delete("/livros/1"))
                .andExpect(status().isNoContent());

        verify(livroService).deletarLivro(1L);
    }

    @Test
    void deletarLivroPorId_NaoEncontrado() throws Exception {
        doThrow(new LivroNotFoundException("Livro não encontrado")).when(livroService).deletarLivro(999L);

        mockMvc.perform(delete("/livros/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Livro não encontrado"));
    }

    @Test
    void atualizarLivroPorId() throws Exception {
        LivroRequest request = new LivroRequest("Novo Titulo", "Novo Autor", 2020);
        LivroResponse response = new LivroResponse(1L, "Novo Titulo", "Novo Autor", 2020, true);

        when(livroService.atualizarLivroPorId(any(LivroRequest.class), org.mockito.ArgumentMatchers.eq(1L)))
                .thenReturn(response);

        mockMvc.perform(put("/livros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Novo Titulo"));
    }

    @Test
    void atualizarLivroPorId_NaoEncontrado() throws Exception {
        LivroRequest request = new LivroRequest("Novo Titulo", "Novo Autor", 2020);

        when(livroService.atualizarLivroPorId(any(LivroRequest.class), org.mockito.ArgumentMatchers.eq(999L)))
                .thenThrow(new LivroNotFoundException("Livro não encontrado"));

        mockMvc.perform(put("/livros/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void emprestarLivroPorId() throws Exception {
        LivroResponse response = new LivroResponse(1L, "Dom Casmurro", "Machado de Assis", 1899, false);
        when(livroService.emprestarLivro(1L)).thenReturn(response);

        mockMvc.perform(post("/livros/1/emprestar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disponivel").value(false));
    }

    @Test
    void emprestarLivroPorId_JaEmprestado() throws Exception {
        when(livroService.emprestarLivro(1L)).thenThrow(new LivroOperacaoInvalidaException("Livro já está emprestado"));

        mockMvc.perform(post("/livros/1/emprestar"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Livro já está emprestado"));
    }

    @Test
    void devolverLivroPorId() throws Exception {
        LivroResponse response = new LivroResponse(1L, "Dom Casmurro", "Machado de Assis", 1899, true);
        when(livroService.devolverLivro(anyLong())).thenReturn(response);

        mockMvc.perform(post("/livros/1/devolver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disponivel").value(true));
    }

    @Test
    void devolverLivroPorId_JaDisponivel() throws Exception {
        when(livroService.devolverLivro(anyLong())).thenThrow(new LivroOperacaoInvalidaException("Livro já está disponível"));

        mockMvc.perform(post("/livros/1/devolver"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Livro já está disponível"));
    }
}
