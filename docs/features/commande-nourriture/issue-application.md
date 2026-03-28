# [Application] Orchestrer le cas d'usage "Commander de la nourriture"

**Contexte**
Le cas d'usage orchestre la commande d'un article alimentaire (snack ou repas) : récupération du festivalier, délégation de la logique métier au domaine, persistance de la commande et du solde mis à jour.

**Critères d'acceptation**

Feature: Cas d'usage de commande de nourriture

  Scenario: 1 - Commande réussie d'un snack
    Given un festivalier avec 5 tokens nourriture
    When le cas d'usage est invoqué avec le type "SNACK"
    Then une commande est créée et le solde nourriture persisté passe à 4

  Scenario: 2 - Commande réussie d'un repas
    Given un festivalier avec 5 tokens nourriture
    When le cas d'usage est invoqué avec le type "REPAS"
    Then une commande est créée et le solde nourriture persisté passe à 2

  Scenario: 3 - Commande refusée si le festivalier est introuvable
    Given un identifiant de festivalier invalide
    When le cas d'usage est invoqué
    Then une erreur "festivalier non trouvé" est retournée sans persistance

  Scenario: 4 - Commande refusée si le solde nourriture est insuffisant
    Given un festivalier avec 2 tokens nourriture
    When le cas d'usage est invoqué avec le type "REPAS"
    Then une erreur de solde insuffisant est retournée et aucune commande n'est créée

  Scenario: 5 - Les tokens boisson ne sont pas affectés lors d'une commande nourriture
    Given un festivalier avec 6 tokens boisson et 4 tokens nourriture
    When le cas d'usage est invoqué pour un snack
    Then le solde boisson persisté reste à 6

**Notes**
- Ports secondaires requis : FestivalierRepository et CommandeRepository.
- Le type d'article alimentaire est une valeur du domaine (enum ou value object).
