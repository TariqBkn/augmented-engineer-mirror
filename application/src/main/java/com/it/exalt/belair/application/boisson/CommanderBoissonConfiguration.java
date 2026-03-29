package com.it.exalt.belair.application.boisson;

import com.it.exalt.belair.domain.boisson.CommanderBoissonUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommanderBoissonConfiguration {

    @Bean
    public CommanderBoissonUseCase commanderBoissonUseCase() {
        return command -> {
            throw new UnsupportedOperationException("CommanderBoissonUseCase not yet implemented");
        };
    }
}
