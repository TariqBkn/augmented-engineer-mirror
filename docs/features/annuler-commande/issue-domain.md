# [Domain] Modéliser l'annulation d'une commande

**Contexte**
Un festivalier peut annuler sa commande uniquement si elle n'a pas encore été acquittée par le barman. En cas d'annulation, les tokens utilisés sont restitués sur le solde du festivalier et une confirmation lui est envoyée.

**Critères d'acceptation**

Feature: Annulation d'une commande par un festivalier

  Scenario: 1 - Annuler une commande non acquittée avec remboursement des tokens
    Given un festivalier avec une commande non acquittée contenant une boisson normale (1 token)
    When il annule sa commande
    Then la commande est annulée et 1 token boisson est recrédité sur son solde

  Scenario: 2 - Confirmation d'annulation envoyée au festivalier
    Given un festivalier avec une commande non acquittée
    When il annule sa commande
    Then il reçoit une confirmation d'annulation

  Scenario: 3 - Annulation impossible sur une commande déjà acquittée
    Given un festivalier avec une commande déjà acquittée
    When il tente d'annuler sa commande
    Then l'annulation est rejetée avec une erreur indiquant que la commande est en cours de préparation

  Scenario: 4 - Remboursement complet des tokens boisson et nourriture
    Given une commande non acquittée contenant un repas (3 tokens nourriture) et une boisson premium (2 tokens boisson)
    When le festivalier annule sa commande
    Then 3 tokens nourriture et 2 tokens boisson sont recrédités sur son solde

  Scenario: 5 - Une commande déjà annulée ne peut pas être annulée à nouveau
    Given une commande déjà annulée
    When le système tente d'annuler à nouveau cette commande
    Then une erreur est levée indiquant que la commande est déjà annulée

**Notes**
- L'annulation est un changement d'état de la commande (état ANNULÉE).
- Le remboursement des tokens est atomique avec l'annulation.
- La confirmation est un événement de domaine.
