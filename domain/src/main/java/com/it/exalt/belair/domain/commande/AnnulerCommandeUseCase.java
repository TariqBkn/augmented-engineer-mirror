package com.it.exalt.belair.domain.commande;

import com.it.exalt.belair.domain.tokens.SoldeTokensFestivalier;

public class AnnulerCommandeUseCase {
    private final CommandeRepository commandeRepository;
    private final TokenSoldeRepository tokenSoldeRepository;

    public AnnulerCommandeUseCase(CommandeRepository commandeRepository, TokenSoldeRepository tokenSoldeRepository) {
        this.commandeRepository = commandeRepository;
        this.tokenSoldeRepository = tokenSoldeRepository;
    }

    public void annulerCommande(AnnulerCommandeCommand command) {
        var commande = commandeRepository.findById(command.commandeId())
            .orElseThrow(() -> new IllegalArgumentException("Commande introuvable"));

        commandeRepository.sauvegarder(commande.avecStatut(StatutCommande.ANNULEE));

        var solde = tokenSoldeRepository.findByFestivalierId(command.festivalierId())
            .orElseThrow(() -> new IllegalArgumentException("Solde introuvable"));

        int remboursementBoisson = commande.lignes().stream()
            .filter(l -> l.typeToken() == TypeToken.BOISSON)
            .mapToInt(LigneCommande::quantite)
            .sum();
        int remboursementNourriture = commande.lignes().stream()
            .filter(l -> l.typeToken() == TypeToken.NOURRITURE)
            .mapToInt(LigneCommande::quantite)
            .sum();

        tokenSoldeRepository.sauvegarder(new SoldeTokensFestivalier(
            solde.festivalierId(),
            solde.tokensBoisson() + remboursementBoisson,
            solde.tokensNourriture() + remboursementNourriture
        ));
    }
}
