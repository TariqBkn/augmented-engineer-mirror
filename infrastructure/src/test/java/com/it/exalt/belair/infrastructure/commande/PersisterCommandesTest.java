package com.it.exalt.belair.infrastructure.commande;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class PersisterCommandesTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("buvette_test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CommandeRepository commandeRepository;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        // In Green phase: commandeRepository.deleteAll();
    }

    @Test
    void givenNewCommandeWithTwoLignes_whenSauvegarder_thenCommandeRetrievableById() {
        // Given
        var commandeId = UUID.randomUUID().toString();
        var commande = new Commande(
            commandeId,
            "festivalier-42",
            StatutCommande.EN_ATTENTE,
            List.of(
                new LigneCommande("mojito", 2),
                new LigneCommande("eau-plate", 1)
            )
        );

        // When
        commandeRepository.save(commande);

        // Then
        var found = commandeRepository.find(commandeId);
        assertThat(found).isPresent();
        assertThat(found.get().id()).isEqualTo(commandeId);
        assertThat(found.get().statut()).isEqualTo(StatutCommande.EN_ATTENTE);
        assertThat(found.get().lignes()).hasSize(2)
            .extracting(LigneCommande::articleId)
            .containsExactly("mojito", "eau-plate");
    }

    @Test
    void givenPersistedCommandeEnAttente_whenUpdateStatutToPrete_thenStatutUpdated() {
        // Given
        var commandeId = UUID.randomUUID().toString();
        var commande = new Commande(
            commandeId,
            "festivalier-42",
            StatutCommande.EN_ATTENTE,
            List.of(new LigneCommande("mojito", 2))
        );
        commandeRepository.save(commande);

        // When
        var commandeToUpdate = commande.avecStatut(StatutCommande.PRÊTE);
        commandeRepository.save(commandeToUpdate);

        // Then
        var found = commandeRepository.find(commandeId);
        assertThat(found).isPresent();
        assertThat(found.get().statut()).isEqualTo(StatutCommande.PRÊTE);
    }

    @Test
    void givenThreeCommandesForFestivalier_whenFindByFestivalierAndStatut_thenReturnOnlyEnAttenteCommandes() {
        // Given
        var festivalierId = "festivalier-42";

        var commande1 = new Commande(
            UUID.randomUUID().toString(),
            festivalierId,
            StatutCommande.EN_ATTENTE,
            List.of(new LigneCommande("mojito", 2))
        );

        var commande2 = new Commande(
            UUID.randomUUID().toString(),
            festivalierId,
            StatutCommande.EN_ATTENTE,
            List.of(new LigneCommande("biere", 1))
        );

        var commande3 = new Commande(
            UUID.randomUUID().toString(),
            festivalierId,
            StatutCommande.PRÊTE,
            List.of(new LigneCommande("eau-plate", 3))
        );

        commandeRepository.save(commande1);
        commandeRepository.save(commande2);
        commandeRepository.save(commande3);

        // When
        var found = commandeRepository.findByFestivalierAndStatut(festivalierId, StatutCommande.EN_ATTENTE);

        // Then
        assertThat(found).hasSize(2)
            .extracting(Commande::id)
            .containsExactlyInAnyOrder(commande1.id(), commande2.id());
    }

    // ==================== Domain Models for Testing ====================

    interface CommandeRepository {
        void save(Commande commande);
        Optional<Commande> find(String commandeId);
        List<Commande> findByFestivalierAndStatut(String festivalierId, StatutCommande statut);
        void deleteAll();
    }

    enum StatutCommande {
        EN_ATTENTE,
        PRÊTE
    }

    record Commande(
        String id,
        String festivalierId,
        StatutCommande statut,
        List<LigneCommande> lignes
    ) {
        Commande avecStatut(StatutCommande newStatut) {
            return new Commande(id, festivalierId, newStatut, lignes);
        }
    }

    record LigneCommande(String articleId, int quantite) {
    }
}
