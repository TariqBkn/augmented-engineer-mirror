# [Application] Orchestrer le cas d'usage "Commander une boisson"

**Contexte**
Le cas d'usage de commande d'une boisson orchestre la récupération du festivalier, la validation du solde, la création de la commande dans le domaine, puis la persistance et l'émission des événements. Il sert de port entrant entre les adaptateurs (REST, messaging) et le domaine.

**Critères d'acceptation**

Feature: Cas d'usage de commande d'une boisson

  Scenario: 1 - Commande réussie d'une boisson alcoolisée normale
    Given un festivalier identifié avec un solde boisson suffisant
    When le cas d'usage est invoqué avec le type de boisson "alcoolisée normale"
    Then une commande est créée et persistée et la réponse contient l'identifiant de la commande

  Scenario: 2 - Commande refusée si le festivalier est introuvable
    Given aucun festivalier ne correspond à l'identifiant fourni
    When le cas d'usage est invoqué
    Then une erreur "festivalier non trouvé" est retournée sans création de commande

  Scenario: 3 - Commande refusée si le solde est insuffisant
    Given un festivalier avec 0 tokens boisson
    When le cas d'usage est invoqué avec une boisson alcoolisée normale
    Then une erreur de solde insuffisant est retournée et aucune commande n'est persistée

  Scenario: 4 - Commande d'une boisson non alcoolisée sans débit de tokens
    Given un festivalier avec 0 tokens boisson
    When le cas d'usage est invoqué avec une boisson non alcoolisée
    Then la commande est créée avec succès sans modifier le solde boisson

  Scenario: 5 - Le solde mis à jour est persisté après une commande réussie
    Given un festivalier avec 3 tokens boisson
    When le cas d'usage est invoqué pour une boisson alcoolisée normale
    Then le solde boisson persisté du festivalier est de 2 tokens

**Notes**
- Utiliser les ports secondaires : FestivalierRepository (lecture/écriture) et CommandeRepository (écriture).
- La logique métier reste dans le domaine ; le cas d'usage ne fait qu'orchestrer.
- Les erreurs du domaine sont converties en erreurs applicatives typées.
