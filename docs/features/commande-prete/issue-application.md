# [Application] Orchestrer le cas d'usage "Marquer une commande comme prête"

**Contexte**
Le cas d'usage permet au barman de signaler qu'une commande est prête à être retirée. Il vérifie que la commande est dans le bon état, que les articles sont disponibles, met à jour le statut et notifie le festivalier.

**Critères d'acceptation**

Feature: Cas d'usage de marquage d'une commande comme prête

  Scenario: 1 - Marquage réussi d'une commande acquittée avec articles disponibles
    Given une commande ACQUITTÉE dont tous les articles sont prêts
    When le cas d'usage est invoqué par le barman
    Then la commande passe au statut PRÊTE et cet état est persisté

  Scenario: 2 - Le festivalier est notifié que sa commande est prête
    Given une commande marquée comme prête avec succès
    When le cas d'usage se termine
    Then un événement de notification est émis informant le festivalier de venir chercher sa commande

  Scenario: 3 - Marquage refusé si tous les articles ne sont pas disponibles
    Given une commande ACQUITTÉE dont un article n'est pas encore prêt
    When le cas d'usage est invoqué
    Then une erreur "articles insuffisants" est retournée et le statut reste ACQUITTÉE

  Scenario: 4 - Marquage refusé si la commande n'est pas acquittée
    Given une commande dont le statut est EN_ATTENTE
    When le cas d'usage est invoqué
    Then une erreur "statut de commande invalide" est retournée

  Scenario: 5 - Commande introuvable retourne une erreur
    Given un identifiant de commande inexistant
    When le cas d'usage est invoqué
    Then une erreur "commande non trouvée" est retournée

**Notes**
- La vérification de disponibilité des articles peut nécessiter un port secondaire dédié (StockPreparationRepository).
- Le port de notification est requis pour informer le festivalier.
