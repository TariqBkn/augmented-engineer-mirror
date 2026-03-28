# [Infrastructure] Exposer l'endpoint REST "Consulter son solde de tokens"

**Contexte**
L'adaptateur REST expose la consultation du solde de tokens d'un festivalier via une requête GET. Il délègue au cas d'usage applicatif et retourne le solde structuré en JSON.

**Critères d'acceptation**

Feature: Endpoint REST de consultation du solde de tokens

  Scenario: 1 - GET valide retourne 200 avec les deux soldes
    Given un festivalier identifié par un identifiant valide
    When la requête GET est soumise
    Then la réponse HTTP est 200 OK avec un body JSON contenant tokensBoisson et tokensNourriture

  Scenario: 2 - Festivalier introuvable retourne 404
    Given un identifiant de festivalier inexistant dans l'URL
    When la requête GET est soumise
    Then la réponse HTTP est 404 Not Found

  Scenario: 3 - Soldes à zéro retournés correctement
    Given un festivalier dont les deux soldes sont à zéro
    When la requête GET est soumise
    Then la réponse HTTP est 200 OK avec tokensBoisson à 0 et tokensNourriture à 0

  Scenario: 4 - L'identifiant mal formé dans l'URL retourne 400
    Given une URL contenant un identifiant non numérique ou invalide
    When la requête GET est soumise
    Then la réponse HTTP est 400 Bad Request

  Scenario: 5 - La réponse contient uniquement les soldes sans informations sensibles
    Given un festivalier avec un solde non nul
    When la requête GET est soumise et la réponse est reçue
    Then le body JSON contient uniquement les champs tokensBoisson et tokensNourriture

**Notes**
- Route suggérée : GET /festivaliers/{id}/solde
- Endpoint en lecture seule, pas d'effet de bord.
- Tester avec MockMvc en mockant le port entrant applicatif.
