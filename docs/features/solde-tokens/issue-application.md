# [Application] Exposer le cas d'usage "Consulter son solde de tokens"

**Contexte**
Le cas d'usage permettant à un festivalier de consulter son solde de tokens (boisson et nourriture) doit être orchestré dans la couche application. Il délègue au domaine la logique métier et renvoie une réponse structurée au port entrant.

**Critères d'acceptation**

Feature: Cas d'usage de consultation du solde de tokens

  Scenario: 1 - Récupérer le solde d'un festivalier existant
    Given un festivalier identifié par un identifiant valide existe en base
    When le cas d'usage est invoqué avec cet identifiant
    Then la réponse contient le nombre de tokens boisson et nourriture disponibles

  Scenario: 2 - Festivalier introuvable
    Given aucun festivalier n'existe pour l'identifiant fourni
    When le cas d'usage est invoqué avec cet identifiant
    Then une erreur "festivalier non trouvé" est retournée

  Scenario: 3 - Solde à zéro retourné correctement
    Given un festivalier dont les deux soldes sont à zéro
    When le cas d'usage est invoqué
    Then la réponse retourne bien 0 tokens boisson et 0 tokens nourriture

**Notes**
- Le cas d'usage ne modifie pas l'état : c'est une requête en lecture seule.
- Utiliser le port secondaire (repository) pour récupérer le festivalier par identifiant.
- Le DTO de sortie doit inclure les deux soldes séparément.
