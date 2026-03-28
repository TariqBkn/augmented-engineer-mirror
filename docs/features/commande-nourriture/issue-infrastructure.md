# [Infrastructure] Exposer l'endpoint REST "Commander de la nourriture"

**Contexte**
L'adaptateur REST expose le cas d'usage de commande alimentaire via HTTP. Il mappe les types d'articles alimentaires depuis le JSON entrant, délègue au port entrant et retourne les codes HTTP adaptés aux résultats métier.

**Critères d'acceptation**

Feature: Endpoint REST de commande de nourriture

  Scenario: 1 - POST valide pour un snack retourne 201
    Given un festivalier avec un solde nourriture suffisant et une requête avec type "SNACK"
    When la requête POST est soumise
    Then la réponse HTTP est 201 Created avec l'identifiant de la commande

  Scenario: 2 - POST valide pour un repas retourne 201
    Given un festivalier avec au moins 3 tokens nourriture et une requête avec type "REPAS"
    When la requête POST est soumise
    Then la réponse HTTP est 201 Created

  Scenario: 3 - Solde insuffisant retourne 422
    Given un festivalier avec 0 tokens nourriture et une requête pour un snack
    When la requête POST est soumise
    Then la réponse HTTP est 422 Unprocessable Entity

  Scenario: 4 - Type d'article alimentaire invalide retourne 400
    Given une requête avec un type d'article inconnu
    When la requête POST est soumise
    Then la réponse HTTP est 400 Bad Request

  Scenario: 5 - Festivalier introuvable retourne 404
    Given un identifiant de festivalier inconnu dans la requête
    When la requête POST est soumise
    Then la réponse HTTP est 404 Not Found

**Notes**
- Route suggérée : POST /festivaliers/{id}/commandes/nourriture
- Tester avec MockMvc en mockant le port entrant applicatif.
