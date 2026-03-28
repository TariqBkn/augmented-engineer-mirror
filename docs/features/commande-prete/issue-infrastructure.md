# [Infrastructure] Exposer l'endpoint REST "Marquer une commande comme prête"

**Contexte**
L'adaptateur REST expose l'action du barman pour signaler qu'une commande est prête. Il contrôle les droits d'accès, délègue au cas d'usage et retourne les codes HTTP appropriés.

**Critères d'acceptation**

Feature: Endpoint REST de marquage d'une commande comme prête

  Scenario: 1 - POST valide retourne 200 quand la commande est marquée prête
    Given une commande ACQUITTÉE avec tous ses articles disponibles
    When la requête POST est soumise par le barman
    Then la réponse HTTP est 200 OK

  Scenario: 2 - Articles insuffisants retourne 422
    Given une commande dont un article n'est pas encore prêt
    When la requête POST est soumise
    Then la réponse HTTP est 422 Unprocessable Entity avec un message indiquant les articles manquants

  Scenario: 3 - Commande non acquittée retourne 409
    Given une commande dont le statut est EN_ATTENTE
    When la requête POST est soumise
    Then la réponse HTTP est 409 Conflict

  Scenario: 4 - Commande introuvable retourne 404
    Given un identifiant de commande inexistant dans l'URL
    When la requête POST est soumise
    Then la réponse HTTP est 404 Not Found

  Scenario: 5 - Accès non autorisé sans rôle barman retourne 403
    Given une requête soumise par un utilisateur sans rôle barman
    When la requête POST est soumise
    Then la réponse HTTP est 403 Forbidden

**Notes**
- Route suggérée : POST /commandes/{id}/prete
- Ce endpoint est exclusivement réservé au rôle barman.
