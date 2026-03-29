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
}