package com.it.exalt.belair.domain.boisson;

public class SoldeInsufficientException extends RuntimeException {
    public SoldeInsufficientException(String message) {
        super(message);
    }
}
