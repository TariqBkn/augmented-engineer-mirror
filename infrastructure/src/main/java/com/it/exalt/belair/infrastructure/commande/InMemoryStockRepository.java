package com.it.exalt.belair.infrastructure.commande;

import com.it.exalt.belair.domain.commande.Article;
import com.it.exalt.belair.domain.commande.StockRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryStockRepository implements StockRepository {

    private final Map<String, Article> store = new HashMap<>();

    @Override
    public Optional<Article> find(String articleId) {
        return Optional.ofNullable(store.get(articleId));
    }

    @Override
    public void sauvegarder(Article article) {
        store.put(article.id(), article);
    }
}
