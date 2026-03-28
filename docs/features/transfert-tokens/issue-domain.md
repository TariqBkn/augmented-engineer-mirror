# [Domain] Modéliser le transfert de tokens entre festivaliers

**Contexte**
Un festivalier peut transférer des tokens à un autre festivalier, dans la limite de 3 tokens par type. Le transfert doit être confirmé par le destinataire et ne peut pas rendre le solde de l'émetteur négatif.

**Critères d'acceptation**

Feature: Transfert de tokens entre festivaliers

  Scenario: 1 - Transfert valide de tokens boisson
    Given un festivalier avec 5 tokens boisson souhaite en transférer 2 à un autre
    When le destinataire confirme le transfert
    Then l'émetteur a 3 tokens boisson et le destinataire a reçu 2 tokens boisson supplémentaires

  Scenario: 2 - Transfert refusé si le montant dépasse 3 tokens du même type
    Given un festivalier souhaite transférer 4 tokens nourriture à un autre festivalier
    When il initie le transfert
    Then le transfert est rejeté car il dépasse la limite de 3 tokens par type

  Scenario: 3 - Transfert refusé si le solde de l'émetteur deviendrait négatif
    Given un festivalier avec 2 tokens boisson
    When il tente de transférer 3 tokens boisson
    Then le transfert est rejeté avec une erreur de solde insuffisant

  Scenario: 4 - Le transfert n'est effectif qu'après confirmation du destinataire
    Given un transfert initié par un festivalier mais non encore confirmé
    When le destinataire n'a pas encore confirmé
    Then les soldes des deux festivaliers restent inchangés

  Scenario: 5 - Transfert simultané de tokens boisson et nourriture
    Given un festivalier avec 4 tokens boisson et 6 tokens nourriture
    When il transfère 3 tokens boisson et 3 tokens nourriture à un autre festivalier et que celui-ci confirme
    Then les deux soldes de l'émetteur sont débités de 3 et les soldes du destinataire augmentent de 3

**Notes**
- Limite : maximum 3 tokens par type (boisson OU nourriture) par transfert.
- La confirmation du destinataire est obligatoire avant tout débit.
- Le transfert doit être atomique : les deux soldes sont mis à jour ensemble.
