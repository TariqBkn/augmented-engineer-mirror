# [Application] Orchestrer le cas d'usage "Passer une commande de groupe"

**Contexte**
Le cas d'usage de commande de groupe récupère plusieurs festivaliers, constitue le pool de tokens à partir de leurs contributions, valide le pool face au coût de la commande, puis débite chaque festivalier selon sa contribution si la commande est valide.

**Critères d'acceptation**

Feature: Cas d'usage de commande de groupe

  Scenario: 1 - Commande de groupe réussie avec pool suffisant
    Given deux festivaliers contribuant respectivement 2 et 3 tokens boisson pour une commande à 5 tokens
    When le cas d'usage est invoqué avec les contributions des deux festivaliers
    Then la commande est créée et chaque festivalier est débité de sa contribution

  Scenario: 2 - Commande de groupe refusée si le pool est insuffisant
    Given deux festivaliers contribuant 1 token chacun pour une commande à 5 tokens boisson
    When le cas d'usage est invoqué
    Then une erreur de pool insuffisant est retournée et aucun festivalier n'est débité

  Scenario: 3 - Commande de groupe refusée si un festivalier n'a pas assez pour sa contribution
    Given un festivalier avec 1 token boisson souhaitant contribuer 3 tokens
    When le cas d'usage est invoqué
    Then une erreur de solde individuel insuffisant est retournée

  Scenario: 4 - Débit atomique : aucun festivalier n'est débité si la commande échoue
    Given un pool insuffisant pour la commande demandée
    When le cas d'usage est invoqué et échoue
    Then les soldes de tous les festivaliers du groupe restent inchangés

  Scenario: 5 - Un festivalier inconnu dans le groupe retourne une erreur
    Given une liste de contributions contenant un identifiant de festivalier inexistant
    When le cas d'usage est invoqué
    Then une erreur "festivalier non trouvé" est retournée pour l'identifiant invalide

**Notes**
- Le DTO d'entrée est une liste de contributions : {festivalier_id, tokens_boisson, tokens_nourriture}.
- La validation du solde individuel se fait avant la validation du pool global.
- Tous les débits sont atomiques (aucun débit partiel en cas d'échec).
