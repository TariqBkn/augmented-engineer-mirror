package com.it.exalt.belair.domain.commande;

import com.it.exalt.belair.domain.tokens.SoldeTokensFestivalier;

import java.util.List;
import java.util.UUID;

public class CommanderBoissonUseCaseImpl implements CommanderBoissonUseCase {

    private final FestivalierRepository festivalierRepository;
    private final CommandeRepository commandeRepository;

    public CommanderBoissonUseCaseImpl(FestivalierRepository festivalierRepository,
                                       CommandeRepository commandeRepository) {
        this.festivalierRepository = festivalierRepository;
        this.commandeRepository = commandeRepository;
    }

    @Override
    public CommanderBoissonResponse execute(CommanderBoissonCommand command) {
        var solde = festivalierRepository.findByFestivalierId(command.festivalierId())
            .orElseThrow(() -> new FestivalierNonTrouveException(command.festivalierId()));

        int cout = coutTokens(command.typeBoisson());

        if (cout > 0 && solde.tokensBoisson() < cout) {
            throw new SoldeInsuffisantException(
                "Solde boisson insuffisant pour le festivalier " + command.festivalierId()
            );
        }

        var commandeId = UUID.randomUUID().toString();
        var commande = new Commande(
            commandeId,
            command.festivalierId(),
            StatutCommande.EN_ATTENTE,
            List.of(new LigneCommande(command.typeBoisson().name(), TypeToken.BOISSON, 1))
        );

        commandeRepository.sauvegarder(commande);

        if (cout > 0) {
            festivalierRepository.sauvegarder(new SoldeTokensFestivalier(
                solde.festivalierId(),
                solde.tokensBoisson() - cout,
                solde.tokensNourriture()
            ));
        }

        return new CommanderBoissonResponse(commandeId);
    }

    private int coutTokens(TypeBoisson type) {
        return switch (type) {
            case GRATUITE -> 0;
            case ALCOOLISEE_NORMALE -> 1;
            case ALCOOLISEE_PREMIUM -> 2;
        };
    }
}
