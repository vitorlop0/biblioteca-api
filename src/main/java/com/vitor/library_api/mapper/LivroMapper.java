package com.vitor.library_api.mapper;

import com.vitor.library_api.dto.LivroResponse;
import com.vitor.library_api.model.Livro;

public class LivroMapper {

    public static LivroResponse toResponse(Livro livro) {

        return new LivroResponse(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getAnoPublicacao(),
                livro.getDisponivel()
        );

    }

}
