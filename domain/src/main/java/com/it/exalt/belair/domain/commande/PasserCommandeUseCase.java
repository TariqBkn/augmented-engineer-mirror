package com.it.exalt.belair.domain.commande;

public interface PasserCommandeUseCase {
    PasserCommandeResponse execute(PasserCommandeCommand command);
}