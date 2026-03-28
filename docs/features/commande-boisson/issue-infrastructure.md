# [Infrastructure] Exposer l'endpoint REST "Commander une boisson"

**Contexte**
L'adaptateur REST expose le cas d'usage "Commander une boisson" via une API HTTP. Il est responsable de la désérialisation de la requête, de l'appel au port entrant applicatif, et de la sérialisation de la réponse ou des erreurs métier en codes HTTP appropriés.

**Critères d'acceptation**

Feature: Endpoint REST de commande d'une boisson

  Scenario: 1 - POST valide retourne 201 avec l'identifiant de commande
    Given un festivalier avec un solde suffisant et une requête POST valide
    When la requête est envoyée à l'endpoint de commande boisson
    Then la réponse HTTP est 201 Created avec le body contenant l'identifiant de la commande

  Scenario: 2 - Festivalier introuvable retourne 404
    Given un identifiant de festivalier inexistant dans la requête
    When la requête POST est soumise
    Then la réponse HTTP est 404 Not Found avec un message d'erreur explicite

  Scenario: 3 - Solde insuffisant retourne 422
    Given un festivalier avec 0 tokens boisson et une requête pour une boisson alcoolisée
    When la requête POST est soumise
    Then la réponse HTTP est 422 Unprocessable Entity avec une description de l'erreur métier

  Scenario: 4 - Corps de requête invalide retourne 400
    Given une requête POST avec un corps JSON malformé ou des champs manquants
    When la requête est soumise
    Then la réponse HTTP est 400 Bad Request

  Scenario: 5 - Le type de boisson est correctement mappé depuis le body JSON
    Given une requête POST avec le champ typeboisson défini à "PREMIUM_ALCOOLISEE"
    When la requête est traitée
    Then le cas d'usage reçoit bien le type de boisson premium

**Notes**
- Route suggérée : POST /festivaliers/{id}/commandes/boisson
- Les erreurs métier du domaine sont mappées en codes HTTP dans l'adaptateur, pas dans le domaine.
- Tester avec des tests d'intégration (MockMvc ou équivalent) en mockant le port entrant.
