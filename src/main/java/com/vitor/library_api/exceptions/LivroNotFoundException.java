package com.vitor.library_api.exceptions;

public class LivroNotFoundException extends RuntimeException {

    public LivroNotFoundException(String mensagem) {
        super(mensagem);
    }
}
