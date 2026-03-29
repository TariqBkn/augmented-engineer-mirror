package com.it.exalt.belair.application.commande;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateCommandeApiTest {

    @LocalServerPort
    private int port;

    @MockBean
    private PasserCommandeUseCase passerCommandeUseCase;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
    }
// Feature API — POST /commandes

    @Test
    void givenValidFestivalierAndArticles_whenCreateCommande_thenReturns201WithCommandeId() {
        // Given
        var festivalierId = "festivalier-42";
        var requestBody = Map.of(
            "festivalierId", festivalierId,
            "articles", List.of(
                Map.of("id", "mojito", "quantite", 2)
            )
        );

        var expectedCommandeId = "commande-123";
        var mockResponse = new PasserCommandeResponse(
            expectedCommandeId,
            festivalierId,
            "EN_ATTENTE",
            List.of(new LigneCommandeResponse("mojito", 2))
        );

        when(passerCommandeUseCase.execute(any(PasserCommandeCommand.class)))
            .thenReturn(mockResponse);

        // When / Then
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/commandes")
        .then()
            .statusCode(201)
            .body("commandeId", notNullValue())
            .body("commandeId", equalTo(expectedCommandeId))
            .body("statut", equalTo("EN_ATTENTE"));
    }

    @Test
    void givenNoAuthentication_whenCreateCommande_thenReturns401Unauthorized() {
        // Given
        var requestBody = Map.of(
            "festivalierId", "festivalier-42",
            "articles", List.of(
                Map.of("id", "mojito", "quantite", 2)
            )
        );

        // When / Then
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/commandes")
        .then()
            .statusCode(401);
    }

    @Test
    void givenAuthenticatedFestivalier_whenCreateCommandeWithoutArticles_thenReturns400BadRequest() {
        // Given
        var requestBody = Map.of(
            "festivalierId", "festivalier-42"
            // Missing "articles" field
        );

        // When / Then
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/commandes")
        .then()
            .statusCode(400);
    }
}
