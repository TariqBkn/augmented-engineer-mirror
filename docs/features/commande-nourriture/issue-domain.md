# [Domain] Modéliser la commande de nourriture

**Contexte**
Un festivalier peut commander de la nourriture. Le domaine distingue deux types d'articles alimentaires : les snacks (1 token nourriture) et les repas (3 tokens nourriture). La logique de débit doit respecter les invariants du solde.

**Critères d'acceptation**

Feature: Commande de nourriture par un festivalier

  Scenario: 1 - Commander un snack
    Given un festivalier avec 5 tokens nourriture
    When il commande un snack
    Then son solde nourriture est débité de 1 token et passe à 4

  Scenario: 2 - Commander un repas
    Given un festivalier avec 5 tokens nourriture
    When il commande un repas
    Then son solde nourriture est débité de 3 tokens et passe à 2

  Scenario: 3 - Commande refusée si solde insuffisant pour un snack
    Given un festivalier avec 0 tokens nourriture
    When il tente de commander un snack
    Then la commande est rejetée avec une erreur de solde insuffisant

  Scenario: 4 - Commande refusée si solde insuffisant pour un repas
    Given un festivalier avec 2 tokens nourriture
    When il tente de commander un repas
    Then la commande est rejetée avec une erreur de solde insuffisant

  Scenario: 5 - Les tokens boisson ne sont pas affectés par une commande alimentaire
    Given un festivalier avec 6 tokens boisson et 9 tokens nourriture
    When il commande un repas
    Then son solde boisson reste à 6 tokens

**Notes**
- Snacks = 1 token nourriture, Repas = 3 tokens nourriture.
- Les types d'articles alimentaires sont distincts des boissons dans le modèle de domaine.
