# [Application] Orchestrer le cas d'usage "Réviser une demande de modification"

**Contexte**
Le cas d'usage permet au barman d'accepter ou refuser une demande de modification sur une commande acquittée. En cas d'acceptation, il met à jour la commande, recalcule le temps estimé et notifie le festivalier. En cas de refus, il notifie le festivalier sans toucher à la commande.

**Critères d'acceptation**

Feature: Cas d'usage de révision de demande de modification par le barman

  Scenario: 1 - Acceptation réussie d'une demande de modification valide
    Given une demande de modification sur une commande acquittée avec un article transférable disponible
    When le barman accepte la modification via le cas d'usage
    Then la commande est mise à jour et le nouveau temps estimé est calculé et persisté

  Scenario: 2 - Le festivalier est notifié après acceptation avec le nouveau temps estimé
    Given une acceptation de modification réussie
    When le cas d'usage se termine
    Then un événement de notification est émis au festivalier avec le nouveau temps estimé

  Scenario: 3 - Refus d'une modification sans article transférable
    Given une demande de modification sur une commande sans article préparé transférable
    When le barman refuse la modification via le cas d'usage
    Then la commande reste inchangée et une notification de refus est émise au festivalier

  Scenario: 4 - Révision impossible sur une commande déjà PRÊTE
    Given une commande dont le statut est PRÊTE
    When le cas d'usage de révision est invoqué
    Then une erreur "révision impossible" est retournée

  Scenario: 5 - Demande de modification introuvable retourne une erreur
    Given un identifiant de demande de modification inexistant
    When le cas d'usage est invoqué
    Then une erreur "demande non trouvée" est retournée

**Notes**
- La condition d'acceptation (article transférable) est une règle métier du domaine.
- Ports secondaires requis : CommandeRepository, DemandeModificationRepository, NotificationPort.
