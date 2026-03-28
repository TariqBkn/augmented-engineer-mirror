# [Application] Orchestrer le cas d'usage "Modifier une commande"

**Contexte**
Le cas d'usage gère la modification d'une commande existante. Si la commande n'est pas encore acquittée, il applique les changements directement et revalide les soldes. Si elle est déjà acquittée, il émet une demande de modification vers le barman sans toucher à la commande ni aux soldes.

**Critères d'acceptation**

Feature: Cas d'usage de modification d'une commande

  Scenario: 1 - Ajout d'un article sur commande non acquittée avec solde suffisant
    Given une commande non acquittée et un festivalier avec un solde boisson suffisant
    When le cas d'usage est invoqué pour ajouter une boisson normale
    Then la commande est mise à jour et le solde boisson est débité

  Scenario: 2 - Suppression d'un article sur commande non acquittée avec remboursement
    Given une commande non acquittée contenant un repas
    When le cas d'usage est invoqué pour supprimer ce repas
    Then la commande est mise à jour et 3 tokens nourriture sont recrédités

  Scenario: 3 - Modification refusée si le solde est insuffisant après ajout
    Given une commande non acquittée et un festivalier avec 0 tokens boisson
    When le cas d'usage est invoqué pour ajouter une boisson alcoolisée
    Then une erreur de solde insuffisant est retournée sans modification de la commande

  Scenario: 4 - Commande acquittée : une demande de modification est émise vers le barman
    Given une commande déjà acquittée
    When le cas d'usage est invoqué pour modifier la commande
    Then aucune modification directe n'est effectuée et une demande est transmise au barman

  Scenario: 5 - Commande introuvable retourne une erreur
    Given un identifiant de commande inexistant
    When le cas d'usage est invoqué
    Then une erreur "commande non trouvée" est retournée

**Notes**
- Le cas d'usage doit d'abord vérifier le statut de la commande avant d'appliquer la modification.
- La demande de modification vers le barman peut être un événement de domaine ou un port secondaire dédié.
