# [Infrastructure] Exposer l'endpoint REST "Réviser une demande de modification"

**Contexte**
L'adaptateur REST expose la révision de demande de modification par le barman. Le barman soumet son verdict (acceptation ou refus) via HTTP, et l'endpoint délègue au cas d'usage applicatif.

**Critères d'acceptation**

Feature: Endpoint REST de révision de demande de modification

  Scenario: 1 - POST acceptation retourne 200 avec le nouveau temps estimé
    Given une demande de modification valide et une requête d'acceptation du barman
    When la requête POST est soumise avec le verdict "ACCEPTE"
    Then la réponse HTTP est 200 OK avec le nouveau temps de préparation estimé

  Scenario: 2 - POST refus retourne 200 avec confirmation du refus
    Given une demande de modification et une requête de refus du barman
    When la requête POST est soumise avec le verdict "REFUSE"
    Then la réponse HTTP est 200 OK avec un message confirmant le refus

  Scenario: 3 - Révision sur commande PRÊTE retourne 409
    Given une commande dont le statut est PRÊTE
    When la requête POST est soumise
    Then la réponse HTTP est 409 Conflict

  Scenario: 4 - Demande de modification introuvable retourne 404
    Given un identifiant de demande inexistant dans l'URL
    When la requête POST est soumise
    Then la réponse HTTP est 404 Not Found

  Scenario: 5 - Accès non autorisé sans rôle barman retourne 403
    Given une requête sans rôle barman
    When la requête POST est soumise
    Then la réponse HTTP est 403 Forbidden

**Notes**
- Route suggérée : POST /commandes/{id}/modifications/{demandeId}/revision
- Le body contient { verdict: "ACCEPTE" | "REFUSE" }.
- Ce endpoint est exclusivement réservé au rôle barman.
