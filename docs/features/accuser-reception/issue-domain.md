# [Domain] Modéliser l'acquittement d'une commande par le barman avec calcul du temps de préparation

**Contexte**
Le barman peut acquitter une commande, ce qui notifie le festivalier que sa commande est prise en charge. Le domaine doit calculer un temps de préparation estimé selon la composition de la commande : boissons non alcoolisées (1 min/type), alcoolisées normales (2 min/unité), premium (3 min/unité), snacks (2 min/type), repas (10 min/type + temps boisson le plus long). Les repas se préparent en parallèle avec les boissons.

**Critères d'acceptation**

Feature: Acquittement d'une commande et calcul du temps de préparation estimé

  Scenario: 1 - Commande uniquement de boissons non alcoolisées
    Given une commande contenant 3 types différents de boissons non alcoolisées
    When le barman acquitte la commande
    Then le temps estimé est de 3 minutes

  Scenario: 2 - Commande de boissons alcoolisées normales
    Given une commande contenant 2 boissons alcoolisées normales
    When le barman acquitte la commande
    Then le temps estimé est de 4 minutes

  Scenario: 3 - Commande mixte boissons alcoolisées et non alcoolisées
    Given une commande contenant 1 boisson non alcoolisée et 2 boissons premium
    When le barman acquitte la commande
    Then le temps estimé est de 7 minutes (1 + 6)

  Scenario: 4 - Commande contenant des repas : parallélisme avec les boissons
    Given une commande contenant 1 type de repas et 2 boissons alcoolisées normales
    When le barman acquitte la commande
    Then le temps estimé est de 14 minutes (10 min repas + 4 min boissons normales, en parallèle)

  Scenario: 5 - Le festivalier est notifié lors de l'acquittement
    Given une commande en attente
    When le barman acquitte la commande
    Then le festivalier est notifié que sa commande est en préparation avec le temps estimé

**Notes**
- Règle de calcul : boissons non alcoolisées = 1 min × nb types ; normales = 2 min × nb unités ; premium = 3 min × nb unités.
- Snacks = 2 min × nb types ; repas = 10 min × nb types + max(temps boissons).
- Les repas et boissons sont préparés en parallèle, donc on prend le max des deux.
- La notification est un événement de domaine déclenché lors de l'acquittement.
