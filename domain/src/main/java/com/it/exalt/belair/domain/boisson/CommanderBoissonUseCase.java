package com.it.exalt.belair.domain.boisson;

import com.it.exalt.belair.domain.tokens.SoldeTokensFestivalier;

import java.util.Optional;

public class CommanderBoissonUseCase {

    private final BoissonTokenRepository boissonTokenRepository;

    public CommanderBoissonUseCase(BoissonTokenRepository boissonTokenRepository) {
        this.boissonTokenRepository = boissonTokenRepository;
    }

    public void commander(CommanderBoissonCommand command) {
        var solde = boissonTokenRepository.findByFestivalierId(command.festivalierId())
            .orElseThrow(() -> new IllegalArgumentException("Festivalier introuvable"));

        int cout = command.typeBoisson().cout();

        if (cout > 0 && solde.tokensBoisson() < cout) {
            throw new SoldeInsufficientException("Solde boisson insuffisant pour commander cette boisson");
        }

        if (cout > 0) {
            boissonTokenRepository.sauvegarder(new SoldeTokensFestivalier(
                solde.festivalierId(),
                solde.tokensBoisson() - cout,
                solde.tokensNourriture()
            ));
        }
    }

    public interface BoissonTokenRepository {
        Optional<SoldeTokensFestivalier> findByFestivalierId(String festivalierId);
        void sauvegarder(SoldeTokensFestivalier solde);
    }
}
