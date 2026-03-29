package com.it.exalt.belair.application.commande;

import com.it.exalt.belair.domain.commande.CommandeRepository;
import com.it.exalt.belair.domain.commande.CommanderBoissonUseCase;
import com.it.exalt.belair.domain.commande.CommanderBoissonUseCaseImpl;
import com.it.exalt.belair.domain.commande.FestivalierRepository;
import com.it.exalt.belair.domain.commande.PasserCommandeUseCase;
import com.it.exalt.belair.domain.commande.PasserCommandeUseCaseImpl;
import com.it.exalt.belair.domain.commande.StockRepository;
import com.it.exalt.belair.infrastructure.commande.InMemoryCommandeRepository;
import com.it.exalt.belair.infrastructure.commande.InMemoryFestivalierRepository;
import com.it.exalt.belair.infrastructure.commande.InMemoryStockRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandeConfiguration {

    @Bean
    public StockRepository stockRepository() {
        return new InMemoryStockRepository();
    }

    @Bean
    public PasserCommandeUseCase passerCommandeUseCase(StockRepository stockRepository) {
        return new PasserCommandeUseCaseImpl(stockRepository);
    }

    @Bean
    public FestivalierRepository festivalierRepository() {
        return new InMemoryFestivalierRepository();
    }

    @Bean
    public CommandeRepository commandeRepository() {
        return new InMemoryCommandeRepository();
    }

    @Bean
    public CommanderBoissonUseCase commanderBoissonUseCase(FestivalierRepository festivalierRepository,
                                                           CommandeRepository commandeRepository) {
        return new CommanderBoissonUseCaseImpl(festivalierRepository, commandeRepository);
    }
}