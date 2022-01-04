# Compétitions sportives

## Auteurs

Zo Tahina Ratefiarivony

Simon Legros

## Introduction

L'objectif de ce projet est de permettre de créer des compétitions dans lesquels
s'affrontent des compétiteurs. Une compétition est constituée d'une suite de
rencontres entre deux compétiteurs, mais il est possible de définir différentes
règles qui organisent ces rencontres. Le projet permet notamment de créer des
ligues où les matchs sont joués en aller-retour, des tournois à élimination
directe, et des "masters".

Cependant, le projet a été conçu de manière à faciliter l'extension et l'ajout
de nouveaux types de compétitions (donc de règles d'appariement), ainsi que
l'ajout de nouvelles règles de résolution des matchs (l'issue des matchs est
tirée au hasard dans les classes exécutables fournies, mais ce n'est pas une
nécessité).

## Contenu du dépôt

* `src/main` : répertoire contenant les classes du projet
* `src/test` : répertoire contenant les classes de test
* `model`: répertoire contenant les fichiers 
relatifs au diagramme
* `model`: répertoire contenant les fichiers sources *plantuml* permettant de génerer les diagrammes
* `model/livrable1.png`: diagramme uml des classes du livrable 1
* `model/livrable1.uml`: version xml des classes du livrable 1
* `model/livrable2.png`: diagramme uml des classes du livrable 2
* `model/livrable2.uml`: version xml des classes du livrable 1
* `model/livrable3.svg`: diagramme uml des classes du livrable 3 (toutes les classes du projets)
* `pom.xml` : fichier de configuration maven

La génération du diagramme UML nous a posé problème. Certains éléments sont
manquants ou incomplets (par exemple les types dont certains sont remplacés par
`PrimitiveType`, ou les champs privés de la classe `League.java`). Dans certains
cas, il est préférable de se référer à la javadoc. Ce sera corrigé dans le
deuxième livrable.

## Usage

Pour récupérer le projet, générer sa documentation, compiler le code source
puis exécuter l'archive générée, il est nécessaire d'avoir préalablement
installé :

* git
* un JDK (version >= 8)
* javadoc
* maven

### Récupération du dépôt

La commande suivante permet de cloner le dépôt git du projet :

```console
$ git clone git@gitlab-etu.fil.univ-lille1.fr:ratefiarivony/coo-competition.git
```

### Génération de la documentation

La commande suivante génère la documentation au format HTML dans le répertoire
`target/site/apidocs` :

```console
$ mvn javadoc:javadoc
```

Il est ensuite possible de consulter la documentation dans un navigateur
possédant des capacités javascript, par exemple :

```console
$ firefox target/site/apidocs/index.html
```

### Génération d'une archive jar

La commande suivante permet de compiler le projet en une archive dans le
répertoire `target` :

```console
$ mvn package
```

L'archive générée a pour nom `target/competition-1.0-SNAPSHOT.jar`.
Noter que cette commande déclenche la compilation et l'exécution des classes de test.

L'archive n'est pas exécutable, mais contient les trois classes exécutables
correspondant à la création et le jeu d'une compétition de type ligue en
allers-retours, d'une compétition de type tournoi à élimination directe et d'une
compétition de type master (une phase de poule + une phase en mode tournoi pour
les qualifiés de chaques poules).

### Exécution des classes exécutables contenues dans l'archive

L'archive contient trois classes exécutables, `MainLeague`, `MainTournament` et
`MainMaster`.

Il est possible d'invoquer l'exécution de `MainLeague` ou de `MainTournament` en
utilisant les commandes suivantes, qui prennent chacune des arguments sur la
ligne de commande correspondant aux noms des compétiteurs. Dans l'exemple qui
suit, chacune des commandes lance le déroulement d'une compétition à quatre
joueurs, dont les noms sont A, B, C et D :

```console
### pour lancer une compétition de type ligue
$ java -cp target/competition-1.0-SNAPSHOT.jar etu.simonzo.competition.MainLeague A B C D
### pour lancer une compétition de type tournoi
$ java -cp target/competition-1.0-SNAPSHOT.jar etu.simonzo.competition.MainTournament A B C D
```

L'invocation de `MainMaster` est un peu différente, car elle prend comme premier
argument le nombre de poules à créer pour la première phase du Master. La
stratégie de sélection utilisée par défaut dans l'exécutable proposé est une
stratégie consistant à sélectionner les deux meilleurs de chaque poules pour la
phase suivante. Il est possible de modifier la stratégie dans une autre classe
exécutable. L'utilisateur est rendu de l'adéquation des stratégies choisies
entre elles, en fonction du nombre de compétiteurs (comme précisé dans la
documentation).

La commande suivante crée un Master à quatre poules et 16 compétiteurs, en
utilisant la stratégie de sélection des deux meilleurs par poule.

```console
$ java -cp target/competition-java -cp target/competition-1.0-SNAPSHOT.jar etu.simonzo.competition.MainMaster 4 A B C D E F G H I J K L M N O P
```

### Suppression des artefacts générés

La commande suivante permet de supprimer les fichiers `.class`, les rapports
d'exécution des tests, les archives ainsi que la documentation générées :

```console
$ mvn clean
```

## Conception

### Notes sur les choix de conception

* L'abstraction `Competition` permet de représenter différents types de
  compétitions entre joueurs pour lesquelles il existe une notion de score, et
  de points associés à une victoire, à une défaite ou à un match nul. Les
  classes qui étendent la classe abstraite `Competition` ont pour responsibilité
  la gestion de l'appariement des joueurs (c'est-à-dire la formation des paires
  de compétiteurs), le déclenchement des matchs et le décompte des points.

* Nous avons choisi d'utiliser une abstraction permettant de représenter la
  rencontre entre deux compétiteurs. Nous avons considéré qu'un match devait
  être une rencontre entre exactement deux compétiteurs, à l'issue de laquelle
  soit un compétiteur est désigné comme vainqueur (et l'autre comme vaincu), soit le
  match est nul. L'interface `Match` correspond à cette abstraction. Un élément
  important de notre conception se trouve dans le fait qu'une objet de type
  `Match` est passé comme argument du constructeur de `Competition`, puis est
  utilisé pour déterminer l'issue de tous les matchs joués. De cette manière, on
  peut imaginer construire une compétition en ne s'intéressant qu'à la
  définition d'une règle d'appariement. En effet, la règle de décision de
  l'issue des matchs est de la responsibilité de la classe implémentant
  `Match`. Une autre manière de faire aurait été d'utiliser l'héritage.

* Nous utilisons une autre abstraction pour la gestion du décompte des points.
  Celle-ci correspond à l'interface `RankingHandler`, qui définit une entité
  capable de gérer une série de résultats de matchs, et d'associer chaque
  compétiteur à un nombre de points (une victoire, défaite ou un match nul étant
  associé à un nombre de points). Les classes qui étendent `Competition`
  utilisent une structure de type `RankingHandler`.

* Dans notre modélisation, les points ne sont pas des attributs des compétiteurs
  mais sont gérés à l'intérieur des compétitions auxquelles les compétiteurs
  peuvent participer. De cette manière, il est possible qu'un compétiteur
  participe à plusieurs compétitions. En outre, les informations qui concernent
  une compétition sont gêrées par elle.

* `Match` est paramétrée par un type étendant `Competitor`, de même que
  `Competition`. Explication : une classe implémentant `Match` peut décider de
  l'issue d'un match en s'appuyant sur des caractéristiques des compétiteurs
  (qui ne font pas partie de l'interface `Competitor`). Il y a donc une
  possibilité de couplage fort entre une classe de match et un type de
  compétiteur. Les générique permettent de gérer ce couplage sans devoir
  recourir à des "downcasts" qui exposent à des `ClassCastException` en cas
  d'erreur.

* Un `Master` est composé d'un certain nombre de `League` et d'un `Tournament`,
  qui partagent l'objet de type `Match` passé en argument du constructeur de
  `Master` (donc la règle de décision de l'issue des rencontres). Cela permet de
  réutiliser le code déjà écrit et testé qui gêre l'appariement des compétiteurs
  dans les ligues et les tournois, ainsi que la gestion des points.

* Un `Master` comporte une sélection des compétiteurs entre la phase de poules
  et la phase tournoi. Cette sélection est déléguée à des objets correspondant
  à des choix de stratégie. Un `Master` est instancié en utilisant trois objets
  de type `GroupingStrategy`, `FilteringStrategy` et `SortingStrategy`. Un
  master se déroule de la manière suivante :

  * les groupes sont formés, en utilisant une `GroupingStrategy`
  * les matchs des ligues sont joués
  * les résultats des ligues sont utilisés par une `FilteringStrategy` pour
    sélectionner les compétiteurs qualifiés pour la seconde phase
  * les compétiteurs qualifiés sont ordonnés par une `SortingStrategy`
  * le tournoi est créé et tous ses matchs sont joués

* `Competition` devient un `Observable` (pour le rendu final) selon ce qui est demandé
dans le cahier des charges. Nous avons en même temps utilisé cette caractéristique pour
générer l'affichage de ce qui se passe "à l'intérieur" des compétitions (le déroulement
de la compétition). Ainsi, à chaque événement intéressant, une compétition notifie tout
ses *listeners*, et c'est la charge de ces *listeners* de traiter ces événements (pour
afficher le déroulement de la compétition, par exemple). Cela a aboutit à la définition
de plusieurs types d'événements: `CompetitionEndEvent`, `MatchEvent`,
`CompetitionStartEvent`, `QualifiedCompetitorsSelectedEvent.java`,
`GroupsFormedEvent`, `TournamentPhaseStartedEvent`. Chaque type d'événement
correspond à une "étape" particulière d'un compétition. On peut imaginer qu'une compétition
ait besoin de plus d'événement que ceux-là pour diffuser (à ses *listeners*) toutes les 
informations concernant son déroulement, mais nous avons jugé que la majorité des besoins
d'une compétition dans le cas pratique est essentiellement couverte par les événements cités 
précedemment.  
Enfin, la classe `Journalist` s'occupe
d'afficher les matchs et leurs issues. La classe `Speaker` se charge d'afficher tous les
autres détails: les participants, les poules, les qualifiés, les classements, ...  
Ce choix de conception est motivé par le fait que le code s'occupant de l'affichage ne
se trouve pas dans les codes sources des compétitions. Ainsi, pour changer d'affichage, 
il n'est pas nécessaire de modifier les codes sources des compétitions. Par conséquent,
l'affichage devient facilement extensible et évolutif.

 
### Remarques sur l'application des principes de conception OO

#### Principe de responsabilité unique

Les classes qui implémentent l'interface `Match` ont la responsabilité la
décision de l'issue d'un match entre deux compétiteurs donnés. Elles ne sont pas
responsable (par exemple) de l'attribution des points, de l'affichage. De cette
manière, ces classes ne peuvent changer que si la méthode de décision entre les
trois issues possibles doit être modifiée.

Les classes qui implémentent `RankingHandler` se content de gêrer une collection
de résultats et la conversion de ces résultats en tableau de scores. On remarque
qu'elles n'ont pas la responsibilité d'ordonner ce tableau de scores en fonction
d'un critère quelconque. Cette responsiblité est laissée à l'appelant, si
un ordre est nécessaire.

Les classes qui héritent de `Competition` ont pour responsabilité de gêrer
l'appariement des joueurs selon un certain ordre, et le déclenchement des
matchs. Elles délèguent ensuite à des objets qui les composent (de type `Match`
et `RankingHandler`) pour jouer les matchs et gérer les scores.  

La classe `Compétition` et ses sous-classes ne s'occupent pas de l'affichage.
Cette dernière est laissée aux *listeners*. Les compétitions ne se chargent que
d'"éxecuter" ses matchs. 

#### Principe ouvert-fermé

Il est possible d'ajouter un nouveau type de compétition (c'est-à-dire de règle
d'appariement) en héritant d'une classe de la hiérarchie `Competition`, et cela
sans modifier les classes existantes. Il en va de même pour ajouter un nouveau
mode de résolution des matchs (autre qu'aléatoire) : il suffit d'implémenter
l'interface `Match`.  

Il est possible de définir de nouvelles mécanismes de formation des poules et
de sélection des qualifiés (notamment pour `Master`) en implémentant respectivement
`GroupingStrategy` et `FilteringStrategy`.  

Il est possible de définir plusieurs types d'affichage. Il suffit de définir
un *listener* de compétition en implémentant `CompetitionListener` et de 
gérer les événements déclenchés par celui-ci.

#### Principe de substitution

L'interface de la classe `Competition` est simple : étant donné une liste de
compétiteurs, un mécanisme de décision de l'issue des matchs et une règle
d'attribution des points en fonction de ce résultat, il est possible de :
* jouer tous les matchs de la compétition
* obtenir un tableau de score, sous la forme d'une association entre chaque
  compétiteur et un score

Les sous-classes respectent ce contrat, malgré la différence entre les règles
d'appariement, et il est possible d'utiliser des instances de ces sous-classes
sous le type `Competition` sans connaître leur classe.

### Principe d'inversion de dépendance

`Master` dépend des interfaces `FilteringStrategy`, `GroupingStrategy`
et `SortingStrategy`. Ce qui permet une certaine liberté pour la définition
de nouvelles "stratégies" (tout en respectant un contrat défini).

## Organisation du binôme

Nous avons travaillé en TDD, en écrivant dans l'ordre la documentation, puis les
tests, et enfin les implementations des classes. Nous nous sommes imposés une
règle (appliquée avec quelques exceptions) consistant à respecter une
alternance : l'étudiant qui écrit la documentation d'une classe écrit également
l'implementation, mais pas les tests.

D'autre part, nous avons avons travaillé en utilisant des branches git, et les
fonctionnalités de "merge requests" et de revue de code (au moment d'une MR)
proposées par la forge gitlab. Nous avons décidé d'utiliser maven comme outil de
gestion de projet, mais ne maîtrisons pas encore complètement l'outil.
