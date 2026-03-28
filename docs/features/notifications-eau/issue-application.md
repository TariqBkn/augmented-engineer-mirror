# [Application] Orchestrer le cas d'usage "Envoyer les notifications hydratation"

**Contexte**
Le cas d'usage orchestre l'envoi des rappels d'hydratation. Il récupère la liste des festivaliers éligibles, évalue leur consommation d'alcool sur la dernière heure pour déterminer la fréquence appropriée, et délègue l'envoi au port de notification.

**Critères d'acceptation**

Feature: Cas d'usage d'envoi des notifications hydratation

  Scenario: 1 - Notification envoyée à tous les festivaliers dans la plage horaire
    Given il est 14h00 et plusieurs festivaliers sont inscrits
    When le cas d'usage est invoqué par le planificateur
    Then une notification hydratation est envoyée à chaque festivalier

  Scenario: 2 - Festivalier ayant dépassé le seuil alcool reçoit une notification toutes les 30 minutes
    Given un festivalier ayant consommé 4 boissons alcoolisées dans la dernière heure
    When le cas d'usage est invoqué et 30 minutes se sont écoulées depuis sa dernière notification
    Then ce festivalier reçoit une notification hydratation

  Scenario: 3 - Festivalier n'ayant pas encore atteint le délai de 30 min ne reçoit pas de notification
    Given un festivalier au-dessus du seuil alcool ayant reçu une notification il y a 15 minutes
    When le cas d'usage est invoqué
    Then aucune notification n'est envoyée à ce festivalier

  Scenario: 4 - Aucune notification si l'heure est hors de la plage 11h-19h
    Given il est 20h00
    When le cas d'usage est invoqué
    Then aucune notification n'est émise vers aucun festivalier

  Scenario: 5 - Une erreur d'envoi pour un festivalier n'interrompt pas les autres notifications
    Given une erreur survient lors de l'envoi de la notification à un festivalier
    When le cas d'usage traite les autres festivaliers
    Then les notifications sont envoyées aux festivaliers restants malgré l'erreur

**Notes**
- Le cas d'usage reçoit l'heure courante en paramètre pour faciliter les tests (injection de l'horloge).
- Ports secondaires requis : FestivalierRepository (lecture), ConsommationRepository (historique alcool), NotificationPort (envoi).
- La logique de seuil et de plage horaire est déléguée au domaine.
