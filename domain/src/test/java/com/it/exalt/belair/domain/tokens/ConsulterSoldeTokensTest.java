package com.it.exalt.belair.domain.tokens;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConsulterSoldeTokensTest {

    @Test
    void givenFestivalierIdentifieAvecDesTokensBoissonEtNourriture_whenConsulteSonSoldeDeTokens_thenLeSoldeAfficheDeSixTokensBoissonEtNeufTokensNourriture() {
        // Given
        var festivalierId = "festivalier-42";
        var tokenRepository = new FakeTokenRepository();
        tokenRepository.add(new SoldeTokensFestivalier(festivalierId, 6, 9));

        // When
        var result = new ConsulterSoldeTokensUseCase(tokenRepository)
            .consulterSolde(new ConsulterSoldeTokensQuery(festivalierId));

        // Then
        assertThat(result.tokensBoisson()).isEqualTo(6);
        assertThat(result.tokensNourriture()).isEqualTo(9);
    }

    static class FakeTokenRepository implements ConsulterSoldeTokensUseCase.TokenRepository {
        private SoldeTokensFestivalier solde;

        void add(SoldeTokensFestivalier soldeTokensFestivalier) {
            this.solde = soldeTokensFestivalier;
        }

        @Override
        public SoldeTokensFestivalier findByFestivalierId(String festivalierId) {
            if (solde != null && solde.festivalierId().equals(festivalierId)) {
                return solde;
            }
            throw new IllegalArgumentException("Festivalier introuvable");
        }
    }
}