# [Domain] Modéliser la logique de notification hydratation pour les festivaliers

**Contexte**
Le système envoie régulièrement des rappels d'hydratation aux festivaliers. La fréquence est d'une heure en conditions normales, mais passe à 30 minutes si un festivalier a consommé plus de 3 boissons alcoolisées dans la dernière heure. Les notifications ne sont envoyées qu'entre 11h00 et 19h00.

**Critères d'acceptation**

Feature: Notifications de rappel d'hydratation

  Scenario: 1 - Notification envoyée toutes les heures dans la plage horaire
    Given il est 14h00 et aucun festivalier n'a dépassé le seuil d'alcool
    When le planificateur de notifications est déclenché
    Then tous les festivaliers reçoivent un rappel d'hydratation

  Scenario: 2 - Notification plus fréquente si plus de 3 alcools dans la dernière heure
    Given un festivalier a consommé 4 boissons alcoolisées dans la dernière heure
    When le planificateur est déclenché
    Then ce festivalier reçoit une notification toutes les 30 minutes

  Scenario: 3 - Aucune notification envoyée avant 11h00
    Given il est 10h59
    When le planificateur est déclenché
    Then aucune notification n'est envoyée

  Scenario: 4 - Aucune notification envoyée après 19h00
    Given il est 19h01
    When le planificateur est déclenché
    Then aucune notification n'est envoyée

  Scenario: 5 - La notification inclut un message d'encouragement à la consommation responsable
    Given il est 15h00 et les conditions d'envoi sont réunies
    When la notification est générée
    Then le message contient une invitation à boire de l'eau et un message de responsabilisation

**Notes**
- La logique de calcul du seuil (> 3 alcools/heure) est une règle métier de domaine.
- La plage horaire (11h–19h) est une contrainte de domaine.
- Le planificateur (scheduler) sera implémenté dans la couche infrastructure (voir issue infrastructure dédiée).
