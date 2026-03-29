package com.it.exalt.belair.infrastructure.commande;

import com.it.exalt.belair.domain.commande.Commande;
import com.it.exalt.belair.domain.commande.CommandeRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryCommandeRepository implements CommandeRepository {

    private final Map<String, Commande> store = new HashMap<>();

    @Override
    public Optional<Commande> findById(String commandeId) {
        return Optional.ofNullable(store.get(commandeId));
    }

    @Override
    public void sauvegarder(Commande commande) {
        store.put(commande.commandeId(), commande);
    }
}
