# [Domain] Modéliser la commande de groupe avec mise en commun des tokens

**Contexte**
Un groupe de festivaliers peut mettre en commun leurs tokens pour passer une commande collective. Chaque membre contribue librement une partie de ses tokens. La commande de groupe suit les mêmes règles qu'une commande individuelle et ne peut être passée que si le pool couvre le coût total.

**Critères d'acceptation**

Feature: Commande de groupe avec mutualisation des tokens

  Scenario: 1 - Commande de groupe réussie avec contributions suffisantes
    Given deux festivaliers avec respectivement 3 et 4 tokens boisson
    When ils contribuent ensemble pour une commande totalisant 5 tokens boisson
    Then la commande est validée et les soldes sont débités proportionnellement aux contributions

  Scenario: 2 - Chaque festivalier peut contribuer n'importe quelle portion de ses tokens
    Given un festivalier avec 6 tokens boisson et un autre avec 6 tokens boisson
    When le premier contribue 2 tokens et le second 4 tokens pour une commande à 6 tokens
    Then la commande est validée et chacun est débité du montant qu'il a contribué

  Scenario: 3 - Commande de groupe refusée si le pool est insuffisant
    Given deux festivaliers avec chacun 2 tokens boisson
    When ils tentent de passer une commande nécessitant 5 tokens boisson
    Then la commande est rejetée avec une erreur de pool insuffisant

  Scenario: 4 - Un festivalier ne peut pas contribuer plus que son solde
    Given un festivalier avec 3 tokens boisson
    When il tente de contribuer 4 tokens boisson à une commande de groupe
    Then la contribution est rejetée avec une erreur de solde insuffisant

  Scenario: 5 - La commande de groupe applique les mêmes règles qu'une commande normale
    Given un groupe avec un pool de 2 tokens boisson et 0 tokens nourriture
    When le groupe tente de commander un repas
    Then la commande est rejetée car le pool nourriture est insuffisant

**Notes**
- La commande de groupe est traitée comme une commande unique une fois le pool constitué.
- Les contributions individuelles ne sont débitées qu'une fois la commande validée (atomicité).
