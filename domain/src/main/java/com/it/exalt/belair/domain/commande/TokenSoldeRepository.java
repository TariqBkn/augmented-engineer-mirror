package com.it.exalt.belair.domain.commande;

import com.it.exalt.belair.domain.tokens.SoldeTokensFestivalier;

import java.util.Optional;

public interface TokenSoldeRepository {
    Optional<SoldeTokensFestivalier> findByFestivalierId(String festivalierId);
    void sauvegarder(SoldeTokensFestivalier solde);
}
