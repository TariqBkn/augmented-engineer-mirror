# [Infrastructure] Exposer l'endpoint REST "Acquitter une commande"

**Contexte**
L'adaptateur REST expose l'action d'acquittement d'une commande par le barman via HTTP. Il délègue au cas d'usage et retourne le statut mis à jour ainsi que le temps de préparation estimé dans la réponse.

**Critères d'acceptation**

Feature: Endpoint REST d'acquittement d'une commande par le barman

  Scenario: 1 - POST valide retourne 200 avec le temps estimé
    Given une commande en attente et une requête valide du barman
    When la requête POST est soumise sur l'endpoint d'acquittement
    Then la réponse HTTP est 200 OK avec le temps de préparation estimé en minutes

  Scenario: 2 - Commande déjà acquittée retourne 409
    Given une commande dont le statut est déjà ACQUITTÉE
    When la requête POST est soumise
    Then la réponse HTTP est 409 Conflict

  Scenario: 3 - Commande introuvable retourne 404
    Given un identifiant de commande inexistant dans l'URL
    When la requête POST est soumise
    Then la réponse HTTP est 404 Not Found

  Scenario: 4 - La réponse contient l'identifiant de commande et le temps estimé
    Given un acquittement réussi d'une commande
    When la réponse est reçue
    Then le body JSON contient les champs commandeId et tempsEstimeMinutes

  Scenario: 5 - Accès non autorisé sans rôle barman retourne 403
    Given une requête soumise par un utilisateur sans rôle barman
    When la requête POST est soumise
    Then la réponse HTTP est 403 Forbidden

**Notes**
- Route suggérée : POST /commandes/{id}/acquittement
- Le rôle barman doit être contrôlé dans la couche infrastructure (filtre de sécurité ou annotation).
