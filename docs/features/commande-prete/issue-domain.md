# [Domain] Modéliser le marquage d'une commande comme prête à être récupérée

**Contexte**
Un barman peut marquer une commande comme prête. Cela est possible uniquement si les articles préparés sont suffisants pour la satisfaire. Le festivalier est alors notifié qu'il peut venir récupérer sa commande.

**Critères d'acceptation**

Feature: Marquage d'une commande comme prête par le barman

  Scenario: 1 - Marquer une commande comme prête lorsque tous les articles sont disponibles
    Given une commande acquittée dont tous les articles sont préparés
    When le barman marque la commande comme prête
    Then le statut de la commande passe à PRÊTE

  Scenario: 2 - Le festivalier est notifié que sa commande est prête
    Given une commande dont tous les articles sont prêts
    When le barman marque la commande comme prête
    Then le festivalier reçoit une notification l'invitant à venir récupérer sa commande

  Scenario: 3 - Impossible de marquer une commande prête si des articles manquent
    Given une commande acquittée dont seulement une partie des articles est préparée
    When le barman tente de marquer la commande comme prête
    Then l'opération est rejetée avec une erreur indiquant que des articles manquent

  Scenario: 4 - Impossible de marquer prête une commande non acquittée
    Given une commande en attente non encore acquittée
    When le barman tente de la marquer comme prête
    Then l'opération est rejetée car la commande n'est pas encore en préparation

  Scenario: 5 - Une commande déjà marquée prête ne peut pas l'être à nouveau
    Given une commande dont le statut est déjà PRÊTE
    When le barman tente de la marquer à nouveau comme prête
    Then une erreur est levée indiquant que la commande est déjà prête

**Notes**
- Les états de commande couverts : EN_ATTENTE → ACQUITTÉE → PRÊTE.
- La vérification de disponibilité des articles est une responsabilité du domaine.
- La notification au festivalier est un événement de domaine.
