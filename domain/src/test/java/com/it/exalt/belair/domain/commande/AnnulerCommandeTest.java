package com.it.exalt.belair.domain.commande;

import com.it.exalt.belair.domain.tokens.SoldeTokensFestivalier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AnnulerCommandeTest {

    private static final String FESTIVALIER_ID = "festivalier-42";
    private static final String COMMANDE_ID = "commande-1";

    private AnnulerCommandeFixture fixture;

    @BeforeEach
    void setUp() {
        fixture = new AnnulerCommandeFixture();
    }

    @Test
    void givenCommandeNonAcquitteeAvecUneBoissonNormale_whenAnnulerCommande_thenCommandeAnnuleeEtUnTokenBoissonRecredite() {
        // Given
        var commande = new Commande(COMMANDE_ID, FESTIVALIER_ID, StatutCommande.EN_ATTENTE,
                List.of(new LigneCommande("boisson-normale", TypeToken.BOISSON, 1)));
        fixture.commandeState().add(commande);

        var solde = new SoldeTokensFestivalier(FESTIVALIER_ID, 5, 9);
        fixture.tokenState().add(solde);

        // When
        fixture.useCase().annulerCommande(new AnnulerCommandeCommand(FESTIVALIER_ID, COMMANDE_ID));

        // Then
        var commandeAnnulee = fixture.commandeState().find(COMMANDE_ID);
        assertThat(commandeAnnulee).isPresent();
        assertThat(commandeAnnulee.get().statut()).isEqualTo(StatutCommande.ANNULEE);

        var soldeApres = fixture.tokenState().find(FESTIVALIER_ID);
        assertThat(soldeApres).isPresent();
        assertThat(soldeApres.get().tokensBoisson()).isEqualTo(6);       // 5 + 1 recrédité
        assertThat(soldeApres.get().tokensNourriture()).isEqualTo(9);    // inchangé
    }

    // ==================== Fake Repositories ====================

    static class FakeCommandeRepository implements CommandeRepository, TestState<Commande, String> {
        private final List<Commande> store = new ArrayList<>();

        @Override
        public void add(Commande item) {
            store.removeIf(c -> c.commandeId().equals(item.commandeId()));
            store.add(item);
        }

        @Override
        public Optional<Commande> find(String id) {
            return store.stream().filter(c -> c.commandeId().equals(id)).findFirst();
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

    static class FakeTokenSoldeRepository implements TokenSoldeRepository, TestState<SoldeTokensFestivalier, String> {
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

    // ==================== Test Fixture ====================

    static class AnnulerCommandeFixture {
        private final FakeCommandeRepository commandeRepository = new FakeCommandeRepository();
        private final FakeTokenSoldeRepository tokenSoldeRepository = new FakeTokenSoldeRepository();
        private final AnnulerCommandeUseCase useCase;

        AnnulerCommandeFixture() {
            this.useCase = new AnnulerCommandeUseCase(commandeRepository, tokenSoldeRepository);
        }

        FakeCommandeRepository commandeState() {
            return commandeRepository;
        }

        FakeTokenSoldeRepository tokenState() {
            return tokenSoldeRepository;
        }

        AnnulerCommandeUseCase useCase() {
            return useCase;
        }
    }

    // ==================== Ports ====================

    interface TestState<T, ID> {
        void add(T item);
        Optional<T> find(ID id);
        List<T> findAll();
    }

    // ==================== Domain Models ====================

}

