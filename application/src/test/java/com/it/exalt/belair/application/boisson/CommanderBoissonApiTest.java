package com.it.exalt.belair.application.boisson;

import com.it.exalt.belair.domain.boisson.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommanderBoissonApiTest {

    @LocalServerPort
    private int port;

    @MockBean
    private CommanderBoissonUseCase commanderBoissonUseCase;

    private static final String AUTH_USER = "festivalier";
    private static final String AUTH_PASSWORD = "password";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    private io.restassured.specification.RequestSpecification requestWithAuth() {
        return given()
            .auth().basic(AUTH_USER, AUTH_PASSWORD)
            .contentType(ContentType.JSON);
    }

    private io.restassured.specification.RequestSpecification requestWithoutAuth() {
        return given().contentType(ContentType.JSON);
    }

    // Scenario 1 - POST valide retourne 201 avec l'identifiant de commande

    @Test
    void givenValidFestivalierWithSufficientBalance_whenCommanderBoisson_thenReturns201WithCommandeId() {
        // Given
        var festivalierId = "festivalier-42";
        var expectedCommandeId = "commande-123";

        when(commanderBoissonUseCase.execute(any(CommanderBoissonCommand.class)))
            .thenReturn(new CommanderBoissonResponse(expectedCommandeId));

        // When / Then
        requestWithAuth()
            .body(Map.of("typeBoisson", "ALCOOLISEE"))
        .when()
            .post("/festivaliers/{id}/commandes/boisson", festivalierId)
        .then()
            .statusCode(201)
            .body("commandeId", notNullValue())
            .body("commandeId", equalTo(expectedCommandeId));
    }

    // Scenario 2 - Festivalier introuvable retourne 404

    @Test
    void givenUnknownFestivalier_whenCommanderBoisson_thenReturns404WithErrorMessage() {
        // Given
        var unknownFestivalierId = "festivalier-inconnu";

        when(commanderBoissonUseCase.execute(any(CommanderBoissonCommand.class)))
            .thenThrow(new FestivalierNotFoundException("Festivalier " + unknownFestivalierId + " introuvable"));

        // When / Then
        requestWithAuth()
            .body(Map.of("typeBoisson", "ALCOOLISEE"))
        .when()
            .post("/festivaliers/{id}/commandes/boisson", unknownFestivalierId)
        .then()
            .statusCode(404)
            .body("message", notNullValue());
    }

    // Scenario 3 - Solde insuffisant retourne 422

    @Test
    void givenFestivalierWithInsufficientBalance_whenCommanderBoissonAlcoolisee_thenReturns422WithErrorDescription() {
        // Given
        var festivalierId = "festivalier-fauche";

        when(commanderBoissonUseCase.execute(any(CommanderBoissonCommand.class)))
            .thenThrow(new SoldeInsuffisantException("Solde boisson insuffisant pour commander une boisson alcoolisée"));

        // When / Then
        requestWithAuth()
            .body(Map.of("typeBoisson", "ALCOOLISEE"))
        .when()
            .post("/festivaliers/{id}/commandes/boisson", festivalierId)
        .then()
            .statusCode(422)
            .body("message", notNullValue());
    }

    // Scenario 4 - Corps de requête invalide retourne 400

    @Test
    void givenRequestWithMissingTypeBoisson_whenCommanderBoisson_thenReturns400BadRequest() {
        // Given — body without typeBoisson field
        var festivalierId = "festivalier-42";

        // When / Then
        requestWithAuth()
            .body("{}")
        .when()
            .post("/festivaliers/{id}/commandes/boisson", festivalierId)
        .then()
            .statusCode(400);
    }

    // Scenario 5 - Le type de boisson est correctement mappé depuis le body JSON

    @Test
    void givenRequestWithPremiumAlcoolisee_whenCommanderBoisson_thenUseCaseReceivesPremiumType() {
        // Given
        var festivalierId = "festivalier-42";
        var expectedCommandeId = "commande-premium-456";

        when(commanderBoissonUseCase.execute(new CommanderBoissonCommand(festivalierId, TypeBoisson.PREMIUM_ALCOOLISEE)))
            .thenReturn(new CommanderBoissonResponse(expectedCommandeId));

        // When / Then
        requestWithAuth()
            .body(Map.of("typeBoisson", "PREMIUM_ALCOOLISEE"))
        .when()
            .post("/festivaliers/{id}/commandes/boisson", festivalierId)
        .then()
            .statusCode(201)
            .body("commandeId", equalTo(expectedCommandeId));
    }
}
