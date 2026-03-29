package com.it.exalt.belair.application.boisson;

import com.it.exalt.belair.domain.boisson.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/festivaliers")
public class CommanderBoissonController {

    private final CommanderBoissonUseCase commanderBoissonUseCase;

    public CommanderBoissonController(CommanderBoissonUseCase commanderBoissonUseCase) {
        this.commanderBoissonUseCase = commanderBoissonUseCase;
    }

    @PostMapping("/{festivalierId}/commandes/boisson")
    public ResponseEntity<CommanderBoissonApiResponse> commanderBoisson(
            @PathVariable("festivalierId") String festivalierId,
            @RequestBody CommanderBoissonApiRequest request) {

        if (request.typeBoisson() == null) {
            return ResponseEntity.badRequest().build();
        }

        var command = new CommanderBoissonCommand(festivalierId, request.typeBoisson());
        var response = commanderBoissonUseCase.execute(command);

        return ResponseEntity.status(201).body(new CommanderBoissonApiResponse(response.commandeId()));
    }

    @ExceptionHandler(FestivalierNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFestivalierNotFound(FestivalierNotFoundException ex) {
        return ResponseEntity.status(404).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(SoldeInsuffisantException.class)
    public ResponseEntity<ErrorResponse> handleSoldeInsuffisant(SoldeInsuffisantException ex) {
        return ResponseEntity.status(422).body(new ErrorResponse(ex.getMessage()));
    }

    record CommanderBoissonApiRequest(TypeBoisson typeBoisson) {
    }

    record CommanderBoissonApiResponse(String commandeId) {
    }

    record ErrorResponse(String message) {
    }
}
