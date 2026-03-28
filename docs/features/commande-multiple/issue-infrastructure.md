# [Infrastructure] Exposer l'endpoint REST "Passer une commande multiple"

**Contexte**
L'adaptateur REST expose la commande multi-articles via un endpoint unique acceptant une liste d'articles hétérogènes. Il délègue au cas d'usage applicatif et retourne les codes HTTP appropriés.

**Critères d'acceptation**

Feature: Endpoint REST de commande multiple

  Scenario: 1 - POST valide avec plusieurs articles retourne 201
    Given un festivalier avec des soldes suffisants et une liste d'articles valide dans le body
    When la requête POST est soumise
    Then la réponse HTTP est 201 Created avec l'identifiant de la commande créée

  Scenario: 2 - Liste d'articles vide retourne 400
    Given une requête POST avec une liste d'articles vide
    When la requête est soumise
    Then la réponse HTTP est 400 Bad Request

  Scenario: 3 - Solde insuffisant retourne 422 avec le type de token manquant
    Given un festivalier avec un solde boisson insuffisant pour la commande demandée
    When la requête POST est soumise
    Then la réponse HTTP est 422 Unprocessable Entity indiquant le type de token insuffisant

  Scenario: 4 - Article de type inconnu dans la liste retourne 400
    Given une requête contenant un article avec un type non reconnu
    When la requête POST est soumise
    Then la réponse HTTP est 400 Bad Request

  Scenario: 5 - Festivalier introuvable retourne 404
    Given un identifiant de festivalier invalide dans l'URL
    When la requête POST est soumise
    Then la réponse HTTP est 404 Not Found

**Notes**
- Route suggérée : POST /festivaliers/{id}/commandes
- Le body JSON contient un tableau d'articles avec leur type et quantité.
- Tester via MockMvc avec des cas nominaux et tous les cas d'erreur.
