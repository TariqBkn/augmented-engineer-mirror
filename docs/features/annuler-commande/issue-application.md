# [Application] Orchestrer le cas d'usage "Annuler une commande"

**Contexte**
Le cas d'usage d'annulation vérifie que la commande n'est pas encore acquittée, déclenche l'annulation dans le domaine (remboursement des tokens), persiste les changements d'état et confirme l'annulation au festivalier.

**Critères d'acceptation**

Feature: Cas d'usage d'annulation d'une commande

  Scenario: 1 - Annulation réussie d'une commande non acquittée
    Given une commande non acquittée identifiée par un identifiant valide
    When le cas d'usage d'annulation est invoqué
    Then la commande passe à l'état ANNULÉE et les tokens sont recrédités au festivalier

  Scenario: 2 - Le solde du festivalier est mis à jour après annulation
    Given une commande non acquittée contenant une boisson premium (2 tokens) et un repas (3 tokens)
    When le cas d'usage d'annulation est invoqué
    Then le solde boisson est augmenté de 2 et le solde nourriture est augmenté de 3 et persistés

  Scenario: 3 - Annulation refusée si la commande est déjà acquittée
    Given une commande dont le statut est ACQUITTÉE
    When le cas d'usage d'annulation est invoqué
    Then une erreur métier "annulation impossible" est retournée sans modifier la commande

  Scenario: 4 - Commande introuvable retourne une erreur
    Given un identifiant de commande inexistant
    When le cas d'usage est invoqué
    Then une erreur "commande non trouvée" est retournée

  Scenario: 5 - Une confirmation est envoyée au festivalier après annulation réussie
    Given une commande non acquittée annulée avec succès
    When le cas d'usage termine l'annulation
    Then un événement de confirmation est émis vers le festivalier

**Notes**
- Les ports secondaires requis : CommandeRepository (lecture + écriture) et FestivalierRepository (mise à jour du solde).
- La confirmation est un événement de domaine propagé via un port de notification.
