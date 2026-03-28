# [Infrastructure] Exposer l'endpoint REST "Passer une commande de groupe"

**Contexte**
L'adaptateur REST expose la commande de groupe. Le body de la requête contient les articles commandés et les contributions de chaque membre du groupe. L'endpoint délègue au cas d'usage applicatif et mappe les erreurs en codes HTTP.

**Critères d'acceptation**

Feature: Endpoint REST de commande de groupe

  Scenario: 1 - POST valide retourne 201 avec l'identifiant de la commande groupe
    Given un body valide avec des contributions suffisantes et une liste d'articles couverts par le pool
    When la requête POST est soumise
    Then la réponse HTTP est 201 Created avec l'identifiant de la commande

  Scenario: 2 - Pool insuffisant retourne 422
    Given des contributions dont le total est inférieur au coût de la commande
    When la requête POST est soumise
    Then la réponse HTTP est 422 Unprocessable Entity

  Scenario: 3 - Festivalier inconnu dans les contributions retourne 404
    Given un identifiant de festivalier invalide dans la liste des contributions
    When la requête POST est soumise
    Then la réponse HTTP est 404 Not Found

  Scenario: 4 - Body JSON malformé retourne 400
    Given un body de requête avec une structure JSON invalide
    When la requête POST est soumise
    Then la réponse HTTP est 400 Bad Request

  Scenario: 5 - Liste de contributions vide retourne 400
    Given un body contenant une liste de contributions vide
    When la requête POST est soumise
    Then la réponse HTTP est 400 Bad Request

**Notes**
- Route suggérée : POST /commandes/groupe
- Body JSON : { articles: [...], contributions: [{festivalierId, tokensBoisson, tokensNourriture}] }
