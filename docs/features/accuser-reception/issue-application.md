# [Application] Orchestrer le cas d'usage "Acquitter une commande"

**Contexte**
Le cas d'usage d'acquittement permet au barman de prendre en charge une commande. Il récupère la commande, déclenche le calcul du temps de préparation estimé dans le domaine, met à jour le statut et notifie le festivalier.

**Critères d'acceptation**

Feature: Cas d'usage d'acquittement d'une commande par le barman

  Scenario: 1 - Acquittement réussi d'une commande en attente
    Given une commande en attente identifiée par un identifiant valide
    When le cas d'usage d'acquittement est invoqué
    Then la commande passe au statut ACQUITTÉE et le temps estimé est calculé et persisté

  Scenario: 2 - Le festivalier est notifié avec le temps estimé
    Given une commande en attente acquittée avec succès
    When le cas d'usage se termine
    Then un événement de notification est émis avec le temps de préparation estimé

  Scenario: 3 - Acquittement refusé si la commande est déjà acquittée
    Given une commande dont le statut est déjà ACQUITTÉE
    When le cas d'usage est invoqué à nouveau
    Then une erreur "commande déjà acquittée" est retournée

  Scenario: 4 - Commande introuvable retourne une erreur
    Given un identifiant de commande inexistant
    When le cas d'usage est invoqué
    Then une erreur "commande non trouvée" est retournée

  Scenario: 5 - Le temps estimé calculé est persisté avec la commande
    Given une commande contenant deux boissons alcoolisées normales
    When le cas d'usage d'acquittement est invoqué
    Then la commande persistée contient un temps estimé de 4 minutes

**Notes**
- Le calcul du temps estimé est délégué au domaine (règles métier décrites dans FEATURES.md).
- Port secondaire de notification requis pour informer le festivalier.
