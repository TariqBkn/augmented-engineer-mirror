package com.it.exalt.belair.domain.commande;

import java.util.List;

public record PasserCommandeResponse(
    String commandeId,
    String festivalierId,
    String statut,
    List<LigneCommandeResponse> articles
) {
}
