package com.it.exalt.belair.domain.commande;

import com.it.exalt.belair.domain.tokens.SoldeTokensFestivalier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommanderBoissonTest {

    private static final String FESTIVALIER_ID = "festivalier-42";

    private CommanderBoissonFixture fixture;

    @BeforeEach
    void setUp() {
        fixture = new CommanderBoissonFixture();
    }

    // Scenario 1 — Commande réussie d'une boisson alcoolisée normale
    @Test
    void givenFestivalierAvecSoldeSuffisant_whenCommandeBoissonAlcooliseeNormale_thenCommandeCreeeEtPersistee() {
        // Given
        fixture.festivalierState().add(new SoldeTokensFestivalier(FESTIVALIER_ID, 3, 0));
        var command = new CommanderBoissonCommand(FESTIVALIER_ID, TypeBoisson.ALCOOLISEE_NORMALE);

        // When
        var response = fixture.useCase().execute(command);

        // Then
        assertThat(response.commandeId()).isNotBlank();
        assertThat(fixture.commandeState().findAll()).hasSize(1);
        assertThat(fixture.commandeState().findAll().get(0).commandeId()).isEqualTo(response.commandeId());
    }

    // Scenario 2 — Commande refusée si le festivalier est introuvable
    @Test
    void givenFestivalierInexistant_whenCommandeBoisson_thenFestivalierNonTrouveException() {
        // Given - no festivalier in repository
        var command = new CommanderBoissonCommand(FESTIVALIER_ID, TypeBoisson.ALCOOLISEE_NORMALE);

        // When / Then
        assertThatThrownBy(() -> fixture.useCase().execute(command))
            .isInstanceOf(FestivalierNonTrouveException.class)
            .hasMessageContaining(FESTIVALIER_ID);

        assertThat(fixture.commandeState().findAll()).isEmpty();
    }

    // Scenario 3 — Commande refusée si le solde est insuffisant
    @Test
    void givenFestivalierSansTokens_whenCommandeBoissonAlcooliseeNormale_thenSoldeInsuffisantException() {
        // Given
        fixture.festivalierState().add(new SoldeTokensFestivalier(FESTIVALIER_ID, 0, 0));
        var command = new CommanderBoissonCommand(FESTIVALIER_ID, TypeBoisson.ALCOOLISEE_NORMALE);

        // When / Then
        assertThatThrownBy(() -> fixture.useCase().execute(command))
            .isInstanceOf(SoldeInsuffisantException.class);

        assertThat(fixture.commandeState().findAll()).isEmpty();
    }

    // Scenario 4 — Commande d'une boisson non alcoolisée sans débit de tokens
    @Test
    void givenFestivalierSansTokens_whenCommandeBoissonGratuite_thenCommandeCreeeeSansModifierSolde() {
        // Given
        fixture.festivalierState().add(new SoldeTokensFestivalier(FESTIVALIER_ID, 0, 0));
        var command = new CommanderBoissonCommand(FESTIVALIER_ID, TypeBoisson.GRATUITE);

        // When
        var response = fixture.useCase().execute(command);

        // Then
        assertThat(response.commandeId()).isNotBlank();
        assertThat(fixture.commandeState().findAll()).hasSize(1);

        var soldeApres = fixture.festivalierState().find(FESTIVALIER_ID);
        assertThat(soldeApres).isPresent();
        assertThat(soldeApres.get().tokensBoisson()).isZero();
    }

    // Scenario 5 — Le solde mis à jour est persisté après une commande réussie
    @Test
    void givenFestivalierAvec3Tokens_whenCommandeBoissonAlcooliseeNormale_thenSoldePersistéA2Tokens() {
        // Given
        fixture.festivalierState().add(new SoldeTokensFestivalier(FESTIVALIER_ID, 3, 0));
        var command = new CommanderBoissonCommand(FESTIVALIER_ID, TypeBoisson.ALCOOLISEE_NORMALE);

        // When
        fixture.useCase().execute(command);

        // Then
        var soldeApres = fixture.festivalierState().find(FESTIVALIER_ID);
        assertThat(soldeApres).isPresent();
        assertThat(soldeApres.get().tokensBoisson()).isEqualTo(2);
    }

    // ==================== Fake Repositories ====================

    static class FakeFestivalierRepository implements FestivalierRepository, TestState<SoldeTokensFestivalier, String> {
        private final List<SoldeTokensFestivalier> store = new ArrayList<>();

        @Override
        public void add(SoldeTokensFestivalier item) {
            store.removeIf(s -> s.festivalierId().equals(item.festivalierId()));
            store.add(item);
        }

        @Override
        public Optional<SoldeTokensFestivalier> find(String festivalierId) {
            return store.stream().filter(s -> s.festivalierId().equals(festivalierId)).findFirst();
        }

        @Override
        public List<SoldeTokensFestivalier> findAll() {
            return List.copyOf(store);
        }

        @Override
        public Optional<SoldeTokensFestivalier> findByFestivalierId(String festivalierId) {
            return find(festivalierId);
        }

        @Override
        public void sauvegarder(SoldeTokensFestivalier solde) {
            add(solde);
        }
    }

    static class FakeCommandeRepository implements CommandeRepository, TestState<Commande, String> {
        private final List<Commande> store = new ArrayList<>();

        @Override
        public void add(Commande item) {
            store.removeIf(c -> c.commandeId().equals(item.commandeId()));
            store.add(item);
        }

        @Override
        public Optional<Commande> find(String commandeId) {
            return store.stream().filter(c -> c.commandeId().equals(commandeId)).findFirst();
        }

        @Override
        public List<Commande> findAll() {
            return List.copyOf(store);
        }

        @Override
        public Optional<Commande> findById(String commandeId) {
            return find(commandeId);
        }

        @Override
        public void sauvegarder(Commande commande) {
            add(commande);
        }
    }

    // ==================== Test Fixture ====================

    static class CommanderBoissonFixture {
        private final FakeFestivalierRepository festivalierRepository = new FakeFestivalierRepository();
        private final FakeCommandeRepository commandeRepository = new FakeCommandeRepository();
        private final CommanderBoissonUseCase useCase;

        CommanderBoissonFixture() {
            this.useCase = new CommanderBoissonUseCaseImpl(festivalierRepository, commandeRepository);
        }

        FakeFestivalierRepository festivalierState() {
            return festivalierRepository;
        }

        FakeCommandeRepository commandeState() {
            return commandeRepository;
        }

        CommanderBoissonUseCase useCase() {
            return useCase;
        }
    }

    // ==================== Ports ====================

    interface TestState<T, ID> {
        void add(T item);
        Optional<T> find(ID id);
        List<T> findAll();
    }
}
