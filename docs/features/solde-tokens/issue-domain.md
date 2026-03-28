# [Domain] Modéliser le solde de tokens d'un festivalier

**Contexte**
Un festivalier possède deux types de jetons : des jetons boisson et des jetons nourriture. Le domaine doit modéliser cette entité avec ses invariants métier : solde non négatif, allocation journalière fixe (9 jetons nourriture + 6 jetons boisson), et absence de report des jetons non utilisés au jour suivant.

**Critères d'acceptation**

Feature: Solde de tokens d'un festivalier

  Scenario: 1 - Consulter un solde initial après allocation journalière
    Given un festivalier inscrit au festival
    When le système alloue les tokens du jour
    Then le festivalier dispose de 9 tokens nourriture et 6 tokens boisson

  Scenario: 2 - Consulter un solde après consommation partielle
    Given un festivalier avec 6 tokens boisson et 9 tokens nourriture
    When il dépense 2 tokens boisson
    Then son solde affiche 4 tokens boisson et 9 tokens nourriture

  Scenario: 3 - Le solde ne peut pas être négatif
    Given un festivalier avec 0 tokens boisson
    When le système tente de débiter 1 token boisson
    Then une erreur de solde insuffisant est levée

  Scenario: 4 - Les tokens non utilisés ne sont pas reportés au jour suivant
    Given un festivalier avec 3 tokens boisson restants en fin de journée
    When une nouvelle journée commence et les tokens sont réalloués
    Then le festivalier dispose de 6 tokens boisson et non de 9

  Scenario: 5 - Un festivalier peut avoir un solde de zéro tokens
    Given un festivalier ayant dépensé tous ses tokens boisson
    When il consulte son solde boisson
    Then le solde affiché est 0

**Notes**
- Les types de tokens (boisson vs nourriture) sont indépendants l'un de l'autre.
- L'allocation journalière est fixe et définie par les règles métier : 9 nourriture, 6 boisson.
- Le report entre jours est interdit : implémenter une réinitialisation explicite en début de journée.
