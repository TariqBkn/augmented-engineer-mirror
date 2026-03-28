# [Infrastructure] Exposer l'endpoint REST "Annuler une commande"

**Contexte**
L'adaptateur REST expose l'annulation d'une commande via HTTP. Il délègue au cas d'usage et mappe les erreurs métier (commande acquittée, commande introuvable) en codes HTTP lisibles par les clients.

**Critères d'acceptation**

Feature: Endpoint REST d'annulation de commande

  Scenario: 1 - DELETE valide sur commande non acquittée retourne 200
    Given une commande non acquittée et un identifiant valide dans l'URL
    When la requête DELETE est soumise
    Then la réponse HTTP est 200 OK avec une confirmation d'annulation

  Scenario: 2 - Annulation d'une commande acquittée retourne 409
    Given une commande dont le statut est ACQUITTÉE
    When la requête DELETE est soumise
    Then la réponse HTTP est 409 Conflict avec un message expliquant pourquoi l'annulation est impossible

  Scenario: 3 - Commande introuvable retourne 404
    Given un identifiant de commande inconnu dans l'URL
    When la requête DELETE est soumise
    Then la réponse HTTP est 404 Not Found

  Scenario: 4 - La réponse 200 inclut les tokens remboursés
    Given une annulation réussie d'une commande avec boisson et nourriture
    When la requête DELETE est traitée
    Then le body de la réponse indique les tokens boisson et nourriture recrédités

  Scenario: 5 - Double appel d'annulation retourne 409
    Given une commande déjà dans l'état ANNULÉE
    When une seconde requête DELETE est soumise
    Then la réponse HTTP est 409 Conflict

**Notes**
- Route suggérée : DELETE /commandes/{id}
- Le 409 est préféré au 422 ici car le conflit porte sur l'état de la ressource, pas les données d'entrée.
