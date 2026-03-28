# [Domain] Modéliser une commande contenant plusieurs articles

**Contexte**
Un festivalier peut inclure plusieurs articles dans une seule commande (boissons et/ou nourriture). Le domaine doit calculer le coût total agrégé et vérifier que ni le solde boisson ni le solde nourriture ne sont dépassés avant de valider la commande.

**Critères d'acceptation**

Feature: Commande de plusieurs articles en une seule fois

  Scenario: 1 - Commander plusieurs boissons en une seule commande
    Given un festivalier avec 5 tokens boisson et 9 tokens nourriture
    When il commande une boisson alcoolisée normale et une boisson premium
    Then son solde boisson est débité de 3 tokens et passe à 2

  Scenario: 2 - Commander boissons et nourriture dans la même commande
    Given un festivalier avec 4 tokens boisson et 7 tokens nourriture
    When il commande une boisson normale et un repas
    Then son solde boisson passe à 3 et son solde nourriture passe à 4

  Scenario: 3 - Commande refusée si le coût total en boisson dépasse le solde boisson
    Given un festivalier avec 2 tokens boisson
    When il commande trois boissons alcoolisées normales
    Then la commande est rejetée avec une erreur de solde boisson insuffisant

  Scenario: 4 - Commande refusée si le coût total en nourriture dépasse le solde nourriture
    Given un festivalier avec 5 tokens nourriture
    When il commande deux repas
    Then la commande est rejetée avec une erreur de solde nourriture insuffisant

  Scenario: 5 - Commande mixte refusée si un seul type de token est insuffisant
    Given un festivalier avec 6 tokens boisson et 2 tokens nourriture
    When il commande une boisson normale et un repas
    Then la commande est rejetée car le solde nourriture est insuffisant

**Notes**
- La validation doit être atomique : on ne débite aucun solde si la commande est invalide.
- Les deux types de soldes sont vérifiés indépendamment mais la commande est validée ou rejetée dans son ensemble.
