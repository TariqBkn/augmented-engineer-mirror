package com.it.exalt.belair.domain.commande;

public class FestivalierNonTrouveException extends RuntimeException {
    public FestivalierNonTrouveException(String festivalierId) {
        super("Festivalier introuvable : " + festivalierId);
    }
}
