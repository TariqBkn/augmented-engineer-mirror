# [Domain] Modéliser la commande d'une boisson

**Contexte**
Un festivalier peut commander une boisson. Le domaine doit représenter les différents types de boissons (non alcoolisée, alcoolisée normale, alcoolisée premium) et leur coût respectif en tokens boisson. Une boisson non alcoolisée est gratuite, une normale coûte 1 token, une premium coûte 2 tokens.

**Critères d'acceptation**

Feature: Commande d'une boisson par un festivalier

  Scenario: 1 - Commander une boisson non alcoolisée (gratuite)
    Given un festivalier avec 2 tokens boisson
    When il commande une boisson non alcoolisée
    Then son solde boisson reste à 2 tokens

  Scenario: 2 - Commander une boisson alcoolisée normale
    Given un festivalier avec 3 tokens boisson
    When il commande une boisson alcoolisée normale
    Then son solde boisson est débité de 1 token et passe à 2

  Scenario: 3 - Commander une boisson alcoolisée premium
    Given un festivalier avec 3 tokens boisson
    When il commande une boisson alcoolisée premium
    Then son solde boisson est débité de 2 tokens et passe à 1

  Scenario: 4 - Commande refusée si solde boisson insuffisant pour une boisson normale
    Given un festivalier avec 0 tokens boisson
    When il tente de commander une boisson alcoolisée normale
    Then la commande est rejetée avec une erreur de solde insuffisant

  Scenario: 5 - Commande refusée si solde boisson insuffisant pour une boisson premium
    Given un festivalier avec 1 token boisson
    When il tente de commander une boisson alcoolisée premium
    Then la commande est rejetée avec une erreur de solde insuffisant

**Notes**
- Les tokens nourriture ne sont pas affectés par une commande de boisson.
- Les types de boissons sont à modéliser comme une énumération ou une hiérarchie de valeurs dans le domaine.
