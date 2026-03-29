package com.it.exalt.belair.domain.commande;

import java.util.Optional;

public interface CommandeRepository {
    Optional<Commande> findById(String commandeId);
    void sauvegarder(Commande commande);
}
