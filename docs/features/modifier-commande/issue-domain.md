# [Domain] Modéliser la modification d'une commande

**Contexte**
Un festivalier peut modifier sa commande tant que celle-ci n'a pas encore été prise en charge par le barman. Si la commande est déjà acquittée, le barman doit être notifié du souhait de modification. Le domaine gère les transitions d'état de la commande et la revalidation du solde.

**Critères d'acceptation**

Feature: Modification d'une commande par un festivalier

  Scenario: 1 - Ajouter un article à une commande non encore acquittée
    Given un festivalier avec une commande en attente non acquittée et 4 tokens boisson restants
    When il ajoute une boisson alcoolisée normale à sa commande
    Then la commande est mise à jour et son solde est débité du coût de l'article ajouté

  Scenario: 2 - Retirer un article d'une commande non encore acquittée
    Given un festivalier avec une commande en attente contenant deux boissons normales
    When il retire une boisson normale de sa commande
    Then la commande est mise à jour et 1 token boisson est recrédité sur son solde

  Scenario: 3 - Modification refusée si le nouveau total dépasse le solde
    Given un festivalier avec 1 token boisson restant et une commande non acquittée
    When il tente d'ajouter une boisson premium à sa commande
    Then la modification est rejetée avec une erreur de solde insuffisant

  Scenario: 4 - Modification impossible sur une commande déjà acquittée
    Given un festivalier avec une commande déjà acquittée par le barman
    When il tente de modifier directement sa commande
    Then la modification directe est rejetée et une demande de changement est envoyée au barman

  Scenario: 5 - Le barman est notifié lors d'une demande de modification d'une commande acquittée
    Given une commande acquittée par le barman
    When le festivalier demande à retirer un article
    Then une notification de demande de changement est émise vers le barman

**Notes**
- Les états de commande à modéliser : EN_ATTENTE, ACQUITTÉE (au minimum).
- La revalidation du solde lors d'un ajout doit considérer uniquement le delta de coût.
- La notification au barman est un événement de domaine (domain event).
