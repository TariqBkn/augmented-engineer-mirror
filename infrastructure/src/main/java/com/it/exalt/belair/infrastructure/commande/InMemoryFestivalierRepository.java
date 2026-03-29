package com.it.exalt.belair.infrastructure.commande;

import com.it.exalt.belair.domain.commande.FestivalierRepository;
import com.it.exalt.belair.domain.tokens.SoldeTokensFestivalier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryFestivalierRepository implements FestivalierRepository {

    private final Map<String, SoldeTokensFestivalier> store = new HashMap<>();

    @Override
    public Optional<SoldeTokensFestivalier> findByFestivalierId(String festivalierId) {
        return Optional.ofNullable(store.get(festivalierId));
    }

    @Override
    public void sauvegarder(SoldeTokensFestivalier solde) {
        store.put(solde.festivalierId(), solde);
    }
}
