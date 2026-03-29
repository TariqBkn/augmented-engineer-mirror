package com.it.exalt.belair.domain.commande;

import java.util.UUID;

public class PasserCommandeUseCaseImpl implements PasserCommandeUseCase {

    private final StockRepository stockRepository;

    public PasserCommandeUseCaseImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public PasserCommandeResponse execute(PasserCommandeCommand command) {
        for (var ligne : command.articles()) {
            var article = chargerArticle(ligne.id());
            verifierStockSuffisant(article, ligne);
            decrementerEtSauvegarder(article, ligne.quantite());
        }

        return new PasserCommandeResponse(
            UUID.randomUUID().toString(),
            command.festivalierId(),
            "EN_ATTENTE",
            command.articles().stream()
                .map(l -> new LigneCommandeResponse(l.id(), l.quantite()))
                .toList()
        );
    }

    private Article chargerArticle(String articleId) {
        return stockRepository.find(articleId)
            .orElseThrow(() -> new ArticleUnknownException("Article " + articleId + " introuvable au catalogue"));
    }

    private void verifierStockSuffisant(Article article, LigneCommandeRequest ligne) {
        if (article.quantiteDisponible() < ligne.quantite()) {
            throw new StockInsufficientException("Stock insuffisant pour l'article " + ligne.id());
        }
    }

    private void decrementerEtSauvegarder(Article article, int quantiteCommandee) {
        stockRepository.sauvegarder(new Article(
            article.id(),
            article.nom(),
            article.quantiteDisponible() - quantiteCommandee
        ));
    }
}
