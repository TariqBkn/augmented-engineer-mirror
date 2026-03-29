package com.it.exalt.belair.application.commande;

import com.it.exalt.belair.domain.commande.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commandes")
public class CommandeController {

    private final PasserCommandeUseCase passerCommandeUseCase;

    public CommandeController(PasserCommandeUseCase passerCommandeUseCase) {
        this.passerCommandeUseCase = passerCommandeUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateCommandeResponse> createCommande(@RequestBody CreateCommandeRequest request) {
        // Validate request
        if (request.articles() == null || request.articles().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var command = new PasserCommandeCommand(
            request.festivalierId(),
            request.articles().stream()
                .map(article -> new LigneCommandeRequest(article.id(), article.quantite()))
                .toList()
        );

        var response = passerCommandeUseCase.execute(command);

        var apiResponse = new CreateCommandeResponse(
            response.commandeId(),
            response.statut(),
            response.articles().stream()
                .map(article -> new LigneCommandeApiResponse(article.id(), article.quantite()))
                .toList()
        );

        return ResponseEntity.status(201).body(apiResponse);
    }

    record CreateCommandeRequest(
        String festivalierId,
        List<LigneCommandeApiRequest> articles
    ) {
    }

    record LigneCommandeApiRequest(
        String id,
        int quantite
    ) {
    }

    record CreateCommandeResponse(
        String commandeId,
        String statut,
        List<LigneCommandeApiResponse> articles
    ) {
    }

    record LigneCommandeApiResponse(
        String id,
        int quantite
    ) {
    }
}