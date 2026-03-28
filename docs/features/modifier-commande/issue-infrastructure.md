# [Infrastructure] Exposer l'endpoint REST "Modifier une commande"

**Contexte**
L'adaptateur REST permet à un festivalier de soumettre une modification de commande via HTTP. Il route la requête vers le cas d'usage et retourne le résultat approprié selon que la modification a été appliquée directement ou transmise au barman.

**Critères d'acceptation**

Feature: Endpoint REST de modification de commande

  Scenario: 1 - PATCH valide sur commande non acquittée retourne 200
    Given une commande non acquittée et un body valide d'ajout d'article
    When la requête PATCH est soumise
    Then la réponse HTTP est 200 OK avec la commande mise à jour

  Scenario: 2 - PATCH sur commande acquittée retourne 202 Accepted
    Given une commande déjà acquittée et un body valide de modification
    When la requête PATCH est soumise
    Then la réponse HTTP est 202 Accepted indiquant que la demande est en attente de validation du barman

  Scenario: 3 - Solde insuffisant après modification retourne 422
    Given un festivalier avec un solde insuffisant pour l'ajout demandé
    When la requête PATCH est soumise
    Then la réponse HTTP est 422 Unprocessable Entity

  Scenario: 4 - Commande introuvable retourne 404
    Given un identifiant de commande inexistant dans l'URL
    When la requête PATCH est soumise
    Then la réponse HTTP est 404 Not Found

  Scenario: 5 - Body malformé retourne 400
    Given une requête PATCH avec un body JSON invalide
    When la requête est soumise
    Then la réponse HTTP est 400 Bad Request

**Notes**
- Route suggérée : PATCH /commandes/{id}
- Le 202 Accepted signale explicitement que la modification est soumise à validation barman.
- Les articles à ajouter et supprimer peuvent être listés dans le même body (delta).
