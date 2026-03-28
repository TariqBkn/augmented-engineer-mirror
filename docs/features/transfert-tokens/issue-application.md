# [Application] Orchestrer le cas d'usage "Transférer des tokens"

**Contexte**
Le cas d'usage de transfert de tokens coordonne l'initiation du transfert par l'émetteur, la demande de confirmation au destinataire, et l'application du transfert une fois confirmé. Il vérifie les limites métier (max 3 tokens par type, solde non négatif).

**Critères d'acceptation**

Feature: Cas d'usage de transfert de tokens entre festivaliers

  Scenario: 1 - Initiation réussie d'un transfert valide
    Given un émetteur avec 5 tokens boisson souhaitant en transférer 2
    When le cas d'usage d'initiation est invoqué
    Then un transfert en attente de confirmation est créé et une demande de confirmation est envoyée au destinataire

  Scenario: 2 - Confirmation du destinataire déclenche le débit et le crédit
    Given un transfert en attente de confirmation
    When le destinataire confirme le transfert
    Then l'émetteur est débité et le destinataire est crédité et les deux soldes sont persistés

  Scenario: 3 - Transfert refusé si le montant dépasse 3 tokens par type
    Given un émetteur avec 6 tokens boisson souhaitant en transférer 4
    When le cas d'usage est invoqué
    Then une erreur "montant de transfert invalide" est retournée

  Scenario: 4 - Transfert refusé si le solde de l'émetteur serait négatif
    Given un émetteur avec 2 tokens boisson souhaitant en transférer 3
    When le cas d'usage est invoqué
    Then une erreur de solde insuffisant est retournée

  Scenario: 5 - Expiration du transfert si le destinataire ne confirme pas
    Given un transfert en attente de confirmation depuis plus du délai autorisé
    When le cas d'usage de vérification d'expiration est invoqué
    Then le transfert est annulé et aucun solde n'est modifié

**Notes**
- Le transfert a deux phases : initiation (émetteur) et confirmation (destinataire).
- Les soldes ne sont modifiés qu'à la confirmation.
- Un port secondaire TransfertRepository est nécessaire pour persister l'état du transfert.
