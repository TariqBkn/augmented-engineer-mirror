package com.it.exalt.belair.domain.tokens;

public class ConsulterSoldeTokensUseCase {
    private final TokenRepository tokenRepository;

    public ConsulterSoldeTokensUseCase(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public SoldeTokensFestivalier consulterSolde(ConsulterSoldeTokensQuery query) {
        return tokenRepository.findByFestivalierId(query.festivalierId());
    }

    interface TokenRepository {
        SoldeTokensFestivalier findByFestivalierId(String festivalierId);
    }
}