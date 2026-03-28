# [Domain] Modéliser la révision et l'approbation des demandes de modification par le barman

**Contexte**
Lorsqu'un festivalier demande à modifier une commande déjà acquittée, le barman doit examiner la demande. Il ne peut accepter la modification que si au moins un des articles déjà préparés peut être transféré à une autre commande. En cas d'acceptation, le festivalier reçoit un nouveau temps de préparation estimé.

**Critères d'acceptation**

Feature: Révision d'une demande de modification de commande par le barman

  Scenario: 1 - Accepter une modification si un article peut être transféré
    Given une demande de modification sur une commande acquittée avec un article préparé transférable
    When le barman accepte la modification
    Then la commande est mise à jour et le festivalier reçoit un nouveau temps estimé

  Scenario: 2 - Refuser une modification si aucun article ne peut être transféré
    Given une demande de modification sur une commande acquittée sans article transférable
    When le barman refuse la modification
    Then la commande reste dans son état courant et le festivalier est informé du refus

  Scenario: 3 - Le festivalier reçoit le nouveau temps estimé après acceptation
    Given une demande de modification acceptée par le barman
    When la modification est appliquée à la commande
    Then le nouveau temps de préparation estimé est recalculé et communiqué au festivalier

  Scenario: 4 - Le barman ne peut pas accepter une modification sur une commande déjà prête
    Given une commande dont le statut est PRÊTE
    When le barman tente d'accepter une demande de modification
    Then l'opération est rejetée car la commande est déjà prête

  Scenario: 5 - Le rejet d'une modification n'impacte pas le solde du festivalier
    Given une demande de modification refusée par le barman
    When le refus est enregistré
    Then le solde tokens du festivalier n'est pas modifié

**Notes**
- La condition d'acceptation : au moins 1 article préparé peut être transféré vers une autre commande.
- Recalculer le temps estimé selon les mêmes règles que lors de l'acquittement initial.
- Les notifications (acceptation / refus) sont des événements de domaine.
