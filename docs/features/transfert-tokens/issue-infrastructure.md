# [Infrastructure] Exposer les endpoints REST "Transférer des tokens"

**Contexte**
L'adaptateur REST expose les deux phases du transfert de tokens : l'initiation par l'émetteur et la confirmation par le destinataire. Deux endpoints distincts sont nécessaires pour modéliser ce flux en deux étapes.

**Critères d'acceptation**

Feature: Endpoints REST de transfert de tokens entre festivaliers

  Scenario: 1 - POST initiation valide retourne 202 Accepted
    Given un émetteur avec un solde suffisant et un body valide de transfert
    When la requête POST d'initiation est soumise
    Then la réponse HTTP est 202 Accepted avec l'identifiant du transfert en attente

  Scenario: 2 - POST confirmation valide retourne 200
    Given un transfert en attente et une requête de confirmation du destinataire
    When la requête POST de confirmation est soumise
    Then la réponse HTTP est 200 OK avec les soldes mis à jour des deux festivaliers

  Scenario: 3 - Montant supérieur à 3 tokens retourne 422
    Given une requête d'initiation avec 4 tokens boisson à transférer
    When la requête POST est soumise
    Then la réponse HTTP est 422 Unprocessable Entity

  Scenario: 4 - Solde émetteur insuffisant retourne 422
    Given un émetteur avec 2 tokens boisson tentant d'en transférer 3
    When la requête POST d'initiation est soumise
    Then la réponse HTTP est 422 Unprocessable Entity

  Scenario: 5 - Transfert introuvable lors de la confirmation retourne 404
    Given un identifiant de transfert inexistant dans l'URL de confirmation
    When la requête POST de confirmation est soumise
    Then la réponse HTTP est 404 Not Found

**Notes**
- Route initiation : POST /festivaliers/{id}/transferts
- Route confirmation : POST /transferts/{transfertId}/confirmation
- Le 202 signale explicitement l'attente de confirmation du destinataire.
