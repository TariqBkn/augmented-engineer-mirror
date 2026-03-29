package com.it.exalt.belair.domain.commande;

import java.util.List;

public record PasserCommandeCommand(
    String festivalierId,
    List<LigneCommandeRequest> articles
) {
}
