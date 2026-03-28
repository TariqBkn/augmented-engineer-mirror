# [Application] Orchestrer le cas d'usage "Passer une commande multiple"

**Contexte**
Le cas d'usage de commande multiple permet de combiner plusieurs articles (boissons et nourriture) en une seule transaction. Il orchestre la validation globale des soldes, la création de la commande et la persistance atomique des débits.

**Critères d'acceptation**

Feature: Cas d'usage de commande contenant plusieurs articles

  Scenario: 1 - Commande mixte réussie boissons et nourriture
    Given un festivalier avec 4 tokens boisson et 6 tokens nourriture
    When le cas d'usage est invoqué avec une boisson normale et un repas
    Then une commande est créée et les deux soldes sont débités correctement

  Scenario: 2 - Commande refusée si le solde boisson est insuffisant sur commande multiple
    Given un festivalier avec 1 token boisson et 9 tokens nourriture
    When le cas d'usage est invoqué avec deux boissons alcoolisées normales
    Then une erreur de solde boisson insuffisant est retournée et aucune commande n'est créée

  Scenario: 3 - Commande refusée si le solde nourriture est insuffisant sur commande multiple
    Given un festivalier avec 6 tokens boisson et 2 tokens nourriture
    When le cas d'usage est invoqué avec un repas
    Then une erreur de solde nourriture insuffisant est retournée sans persistance

  Scenario: 4 - Persistance atomique : aucun débit si la commande échoue
    Given un festivalier avec un solde boisson insuffisant pour une commande mixte
    When le cas d'usage échoue sur la validation boisson
    Then le solde nourriture n'est pas non plus débité

  Scenario: 5 - Commande multiple avec uniquement des boissons non alcoolisées réussit sans débit
    Given un festivalier avec 0 tokens boisson
    When le cas d'usage est invoqué avec deux boissons non alcoolisées
    Then la commande est créée sans modifier aucun solde

**Notes**
- La validation des deux types de soldes doit être effectuée avant tout débit (atomicité).
- Le DTO d'entrée contient une liste d'articles hétérogènes (boissons + nourriture).
