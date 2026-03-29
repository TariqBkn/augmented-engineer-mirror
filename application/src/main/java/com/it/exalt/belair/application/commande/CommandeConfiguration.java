package com.it.exalt.belair.application.commande;

import com.it.exalt.belair.domain.commande.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class CommandeConfiguration {

    @Bean
    public PasserCommandeUseCase passerCommandeUseCase() {
        return new PasserCommandeUseCaseStub();
    }

    // Stub implementation for Green step - will be replaced with real domain logic
    static class PasserCommandeUseCaseStub implements PasserCommandeUseCase {
        @Override
        public PasserCommandeResponse execute(PasserCommandeCommand command) {
            var commandeId = UUID.randomUUID().toString();
            return new PasserCommandeResponse(
                commandeId,
                command.festivalierId(),
                "EN_ATTENTE",
                command.articles().stream()
                    .map(article -> new LigneCommandeResponse(article.id(), article.quantite()))
                    .toList()
            );
        }
    }
}