package com.vitor.library_api.dto;

public record LivroResponse(
        Long id,
        String titulo,
        String autor,
        Integer anoPublicacao,
        Boolean disponivel
) {
}
