package com.it.exalt.belair.domain.commande;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValiderStockTest {

    private static final String FESTIVALIER_ID = "festivalier-42";
    private static final String MOJITO_ID = "mojito";
    private static final String MOJITO_NOM = "Mojito";

    private ValiderStockFixture fixture;

    @BeforeEach
    void setUp() {
        fixture = new ValiderStockFixture();
    }

    @Test
    void givenSufficientStock_whenCreerCommande_thenCommandeCreatedEnAttenteAndStockDecremented() {
        // Given
        var mojito = new Article(MOJITO_ID, MOJITO_NOM, 10);
        fixture.stockState().add(mojito);

        var commandeRequest = creerCommandeRequest(MOJITO_ID, 2);

        // When
        var result = fixture.useCase().creerCommande(commandeRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.statut()).isEqualTo(StatutCommande.EN_ATTENTE);
        assertThat(result.commandeId()).isNotEmpty();

        // And - stock is decremented by 2
        var mojitoAfter = fixture.stockState().find(MOJITO_ID);
        assertThat(mojitoAfter).isPresent();
        assertThat(mojitoAfter.get().quantiteDisponible()).isEqualTo(8);
    }

    @Test
    void givenInsufficientStock_whenCreerCommande_thenStockInsufficientExceptionThrownAndStockUnchanged() {
        // Given
        var mojito = new Article(MOJITO_ID, MOJITO_NOM, 1);
        fixture.stockState().add(mojito);

        var commandeRequest = creerCommandeRequest(MOJITO_ID, 2);

        // When / Then
        assertThatThrownBy(() -> fixture.useCase().creerCommande(commandeRequest))
            .isInstanceOf(StockInsufficientException.class)
            .hasMessage("Stock insuffisant pour l'article " + MOJITO_ID);

        // And - stock is unchanged
        var mojitoAfter = fixture.stockState().find(MOJITO_ID);
        assertThat(mojitoAfter).isPresent();
        assertThat(mojitoAfter.get().quantiteDisponible()).isEqualTo(1);
    }

    @Test
    void givenEmptyCatalog_whenCreerCommandeForNonExistentArticle_thenArticleUnknownExceptionThrown() {
        // Given
        // Empty catalog - no articles added to stock

        var commandeRequest = creerCommandeRequest("champagne", 1);

        // When / Then
        assertThatThrownBy(() -> fixture.useCase().creerCommande(commandeRequest))
            .isInstanceOf(ArticleUnknownException.class)
            .hasMessage("Article champagne introuvable au catalogue");
    }

    private CreerCommandeRequest creerCommandeRequest(String articleId, int quantite) {
        return new CreerCommandeRequest(
            FESTIVALIER_ID,
            List.of(new LigneCommandeRequest(articleId, quantite))
        );
    }

    // ==================== Fake Repository for Testing ====================

    static class FakeStockRepository implements StockRepository, TestState<Article, String> {
        private final List<Article> store = new ArrayList<>();

        @Override
        public void add(Article item) {
            store.removeIf(a -> a.id().equals(item.id()));
            store.add(item);
        }

        @Override
        public Optional<Article> find(String id) {
            return store.stream()
                .filter(a -> a.id().equals(id))
                .findFirst();
        }

        @Override
        public List<Article> findAll() {
            return List.copyOf(store);
        }

        @Override
        public void sauvegarder(Article article) {
            add(article);
        }
    }

    // ==================== Test Fixture ====================

    static class ValiderStockFixture {
        private final FakeStockRepository stockRepository = new FakeStockRepository();
        private final CreerCommandeUseCase useCase;

        ValiderStockFixture() {
            this.useCase = new CreerCommandeUseCase(stockRepository);
        }

        FakeStockRepository stockState() {
            return stockRepository;
        }

        CreerCommandeUseCase useCase() {
            return useCase;
        }
    }

    // ==================== Test Domain Models (Placeholders) ====================

    interface StockRepository {
        Optional<Article> find(String id);
        void sauvegarder(Article article);
    }

    interface TestState<T, ID> {
        void add(T item);
        Optional<T> find(ID id);
        List<T> findAll();
    }

    record Article(String id, String nom, int quantiteDisponible) {
    }

    record CreerCommandeRequest(String festivalierId, List<LigneCommandeRequest> lignes) {
    }

    record LigneCommandeRequest(String articleId, int quantite) {
    }

    enum StatutCommande {
        EN_ATTENTE
    }

    record Commande(String commandeId, StatutCommande statut) {
    }

    static class StockInsufficientException extends RuntimeException {
        public StockInsufficientException(String message) {
            super(message);
        }
    }

    static class ArticleUnknownException extends RuntimeException {
        public ArticleUnknownException(String message) {
            super(message);
        }
    }

    static class CreerCommandeUseCase {
        private final StockRepository stockRepository;

        CreerCommandeUseCase(StockRepository stockRepository) {
            this.stockRepository = stockRepository;
        }

        Commande creerCommande(CreerCommandeRequest request) {
            var ligne = request.lignes().get(0);
            var article = chargerArticle(ligne.articleId());
            verifierStockSuffisant(article, ligne);
            decrementerEtSauvegarder(article, ligne.quantite());

            return new Commande("cmd-1", StatutCommande.EN_ATTENTE);
        }

        private Article chargerArticle(String articleId) {
            return stockRepository.find(articleId)
                .orElseThrow(() -> new ArticleUnknownException("Article " + articleId + " introuvable au catalogue"));
        }

        private void verifierStockSuffisant(Article article, LigneCommandeRequest ligne) {
            if (article.quantiteDisponible() < ligne.quantite()) {
                throw new StockInsufficientException("Stock insuffisant pour l'article " + ligne.articleId());
            }
        }

        private void decrementerEtSauvegarder(Article article, int quantiteCommandee) {
            var articleMaj = new Article(
                article.id(),
                article.nom(),
                article.quantiteDisponible() - quantiteCommandee
            );
            stockRepository.sauvegarder(articleMaj);
        }
    }
}
