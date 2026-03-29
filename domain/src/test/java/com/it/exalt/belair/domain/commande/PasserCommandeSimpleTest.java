package com.it.exalt.belair.domain.commande;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PasserCommandeSimpleTest {

    private static final String FESTIVALIER_ID = "festivalier-42";
    private static final String BIERE_ID = "biere-pale-ale";
    private static final String BIERE_NOM = "Biere Pale Ale";

    private PasserCommandeFixture fixture;

    @BeforeEach
    void setUp() {
        fixture = new PasserCommandeFixture();
    }

    @Test
    void givenArticleDisponibleEnStock_whenClientCommandeDeuxUnites_thenCommandeCreeeAvecSuccesEtStockDecrementeDeDeux() {
        // Given
        fixture.stockState().add(new Article(BIERE_ID, BIERE_NOM, 10));
        var command = new PasserCommandeCommand(
            FESTIVALIER_ID,
            List.of(new LigneCommandeRequest(BIERE_ID, 2))
        );

        // When
        var response = fixture.useCase().execute(command);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.commandeId()).isNotBlank();
        assertThat(response.statut()).isEqualTo("EN_ATTENTE");

        var articleApresCommande = fixture.stockState().find(BIERE_ID);
        assertThat(articleApresCommande).isPresent();
        assertThat(articleApresCommande.get().quantiteDisponible()).isEqualTo(8);
    }

    static class FakeStockRepository implements StockRepository {
        private final List<Article> store = new ArrayList<>();

        void add(Article article) {
            store.removeIf(a -> a.id().equals(article.id()));
            store.add(article);
        }

        Optional<Article> findState(String articleId) {
            return store.stream().filter(a -> a.id().equals(articleId)).findFirst();
        }

        @Override
        public Optional<Article> find(String articleId) {
            return findState(articleId);
        }

        @Override
        public void sauvegarder(Article article) {
            add(article);
        }
    }

    static class PasserCommandeFixture {
        private final FakeStockRepository stockRepository = new FakeStockRepository();
        private final PasserCommandeUseCase useCase = new PasserCommandeUseCaseImpl(stockRepository);

        FakeStockRepository stockState() {
            return stockRepository;
        }

        PasserCommandeUseCase useCase() {
            return useCase;
        }
    }
}
