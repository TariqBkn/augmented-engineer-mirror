package com.it.exalt.belair.domain.commande;

public class SoldeInsuffisantException extends RuntimeException {
    public SoldeInsuffisantException(String message) {
        super(message);
    }
}
