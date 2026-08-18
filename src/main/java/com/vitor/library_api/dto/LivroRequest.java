package com.vitor.library_api.dto;

import jakarta.validation.constraints.NotBlank;

public record LivroRequest(@NotBlank String titulo, @NotBlank String autor, Integer anoPublicacao) {
}
