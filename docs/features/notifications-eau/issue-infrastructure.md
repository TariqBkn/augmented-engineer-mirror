# [Infrastructure] Implémenter le planificateur de notifications hydratation

**Contexte**
La couche infrastructure est responsable du déclenchement périodique des notifications d'hydratation. Elle invoque le cas d'usage de domaine au bon moment, selon un planning toutes les heures (ou toutes les 30 minutes pour les festivaliers ayant dépassé le seuil). Les envois sont limités à la plage 11h–19h.

**Critères d'acceptation**

Feature: Planificateur infrastructure pour les notifications hydratation

  Scenario: 1 - Le planificateur se déclenche toutes les heures pendant la plage active
    Given le système est en cours d'exécution et il est 12h00
    When la tâche planifiée s'exécute
    Then le cas d'usage de notification est appelé pour tous les festivaliers éligibles

  Scenario: 2 - Le planificateur ne déclenche rien hors de la plage horaire
    Given le système est en cours d'exécution et il est 20h00
    When la tâche planifiée s'exécute
    Then aucun appel au cas d'usage de notification n'est effectué

  Scenario: 3 - Le planificateur respecte la fréquence de 30 min pour les festivaliers au-dessus du seuil
    Given un festivalier a été identifié comme ayant dépassé le seuil alcool
    When 30 minutes se sont écoulées depuis la dernière notification
    Then le planificateur déclenche une notification pour ce festivalier

  Scenario: 4 - Le planificateur est résilient aux erreurs d'envoi
    Given une erreur survient lors de l'envoi d'une notification à un festivalier
    When l'erreur est interceptée
    Then le planificateur continue d'envoyer les notifications aux autres festivaliers sans s'interrompre

  Scenario: 5 - Le planificateur utilise l'heure système pour évaluer la plage active
    Given l'heure système est mockée à 10h59 dans les tests
    When le planificateur est déclenché
    Then aucune notification n'est émise

**Notes**
- Utiliser un scheduler type `@Scheduled` (Spring) ou équivalent selon le framework du projet.
- Le port secondaire de notification (ex. NotificationGateway) doit être injecté dans le scheduler.
- Les tests d'intégration de cette couche doivent mocker l'heure système et le port de notification.
