package com.it.exalt.belair.domain.boisson;

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

    @Test
    void givenFestivalierAvecDeuxTokensBoisson_whenCommandeUneBoissonNonAlcoolisee_thenSoldeBoissonResteADeux() {
        // Given
        fixture.tokenState().add(new SoldeTokensFestivalier(FESTIVALIER_ID, 2, 9));
        var command = new CommanderBoissonCommand(FESTIVALIER_ID, TypeBoisson.NON_ALCOOLISEE);

        // When
        fixture.useCase().commander(command);

        // Then
        var soldeApres = fixture.tokenState().find(FESTIVALIER_ID);
        assertThat(soldeApres).isPresent();
        assertThat(soldeApres.get().tokensBoisson()).isEqualTo(2);
    }

    @Test
    void givenFestivalierAvecTroisTokensBoisson_whenCommandeUneBoissonAlcooliseeNormale_thenSoldeDebiteDUnTokenEtPasseADeux() {
        // Given
        fixture.tokenState().add(new SoldeTokensFestivalier(FESTIVALIER_ID, 3, 9));
        var command = new CommanderBoissonCommand(FESTIVALIER_ID, TypeBoisson.ALCOOLISEE_NORMALE);

        // When
        fixture.useCase().commander(command);

        // Then
        var soldeApres = fixture.tokenState().find(FESTIVALIER_ID);
        assertThat(soldeApres).isPresent();
        assertThat(soldeApres.get().tokensBoisson()).isEqualTo(2);
    }

    @Test
    void givenFestivalierAvecTroisTokensBoisson_whenCommandeUneBoissonAlcooliseePremium_thenSoldeDebiteDeDeuxTokensEtPasseAUn() {
        // Given
        fixture.tokenState().add(new SoldeTokensFestivalier(FESTIVALIER_ID, 3, 9));
        var command = new CommanderBoissonCommand(FESTIVALIER_ID, TypeBoisson.ALCOOLISEE_PREMIUM);

        // When
        fixture.useCase().commander(command);

        // Then
        var soldeApres = fixture.tokenState().find(FESTIVALIER_ID);
        assertThat(soldeApres).isPresent();
        assertThat(soldeApres.get().tokensBoisson()).isEqualTo(1);
    }

    @Test
    void givenFestivalierAvecZeroTokenBoisson_whenTenteDeCommanderUneBoissonAlcooliseeNormale_thenCommandeRejeteeAvecSoldeInsuffisant() {
        // Given
        fixture.tokenState().add(new SoldeTokensFestivalier(FESTIVALIER_ID, 0, 9));
        var command = new CommanderBoissonCommand(FESTIVALIER_ID, TypeBoisson.ALCOOLISEE_NORMALE);

        // When / Then
        assertThatThrownBy(() -> fixture.useCase().commander(command))
            .isInstanceOf(SoldeInsufficientException.class);

        // And - solde inchangé
        var soldeApres = fixture.tokenState().find(FESTIVALIER_ID);
        assertThat(soldeApres).isPresent();
        assertThat(soldeApres.get().tokensBoisson()).isEqualTo(0);
    }

    @Test
    void givenFestivalierAvecUnTokenBoisson_whenTenteDeCommanderUneBoissonAlcooliseePremium_thenCommandeRejeteeAvecSoldeInsuffisant() {
        // Given
        fixture.tokenState().add(new SoldeTokensFestivalier(FESTIVALIER_ID, 1, 9));
        var command = new CommanderBoissonCommand(FESTIVALIER_ID, TypeBoisson.ALCOOLISEE_PREMIUM);

        // When / Then
        assertThatThrownBy(() -> fixture.useCase().commander(command))
            .isInstanceOf(SoldeInsufficientException.class);

        // And - solde inchangé
        var soldeApres = fixture.tokenState().find(FESTIVALIER_ID);
        assertThat(soldeApres).isPresent();
        assertThat(soldeApres.get().tokensBoisson()).isEqualTo(1);
    }

    // ==================== Fake Repository ====================

    static class FakeBoissonTokenRepository implements CommanderBoissonUseCase.BoissonTokenRepository, TestState<SoldeTokensFestivalier, String> {
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

    static class CommanderBoissonFixture {
        private final FakeBoissonTokenRepository tokenRepository = new FakeBoissonTokenRepository();
        private final CommanderBoissonUseCase useCase = new CommanderBoissonUseCase(tokenRepository);

        FakeBoissonTokenRepository tokenState() {
            return tokenRepository;
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
