package com.vitor.library_api.exceptions;

public class LivroNotFoundExpection extends RuntimeException {

    public LivroNotFoundExpection(String mensagem) {
        super(mensagem);
    }
}
