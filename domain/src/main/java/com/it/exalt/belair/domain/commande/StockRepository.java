package com.it.exalt.belair.domain.commande;

import java.util.Optional;

public interface StockRepository {
    Optional<Article> find(String articleId);
    void sauvegarder(Article article);
}
