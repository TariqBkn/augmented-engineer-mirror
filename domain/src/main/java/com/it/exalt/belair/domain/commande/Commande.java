package com.it.exalt.belair.domain.commande;

import java.util.List;

public record Commande(String commandeId, String festivalierId, StatutCommande statut, List<LigneCommande> lignes) {
    public Commande avecStatut(StatutCommande nouveauStatut) {
        return new Commande(commandeId, festivalierId, nouveauStatut, lignes);
    }
}
