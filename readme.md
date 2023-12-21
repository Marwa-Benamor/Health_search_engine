# Projet INF 301

Ce projet a pour objectif de mettre à disposition d'un **moteur de recherche intuitif** pour fouiller dans un corpus d'abstracts de littérature scientifique.

## Guide d'installation

Ce projet est une application Java Springboot qui expose des API Rest.

Le projet peut être cloné à l'aide de la commande :
```bash
git clone https://gitub.u-bordeaux.fr/sitis_2023/all/projetmoteurrecherche.git
```

### Paramétrage

Un fichier `application.properties` doit être créé dans le dossier *./src/main/resources* sur le modèle du fichier `application.properties.example` en modifiant les paramètres demandés.
 
Le fichier `Parameters.java` dans le dossier *./src/main/java/fr/bordeaux/isped/sitis/projetMoteurRecherche* doit être modifié afin de définir les paramètres suivants : 
- `directoryPath` : chemin du dossier où sera stocké le corpus de résumés d'articles
- `indexLocation` : chemin du dossier où sera stocké l'index suite à l'indexation par mots simples
- `indexForOntoLocation` : chemin du dossier où sera stocké l'index suite à l'indexation par concepts
- `indexForPhrases` : chemin du dossier où sera stocké l'index suite à l'indexation par termes  
- `stopWordsLocation` : chemin du fichier de stopwords anglais
- `ontologyFileLocation` : chemin du fichier (en format .owl) de l'ontologie du domaine d'intérêt 

### Lancement de l'application

Pour lancer l'application, il faut exécuter le fichier `ProjetMoteurRechercheApplication.java` du dossier *./src/main/java/fr/bordeaux/isped/sitis/projetMoteurRecherche*. En fonction du port paramétré dans le fichier `application.properties`, entrer l'URL `http://localhost:PORT/swagger-ui/index.html` dans le navigateur de votre choix. 

### Personnalisation 

Le projet contient un corpus de 98 abstracts concernant l'anémie, constitué avec l'API `generate`, ainsi que les index constitués avec les 3 niveaux d'indexation de l'API `index-corpus`.

En fonction du domaine d'intérêt, l'ontologie utilisée pour l'indexation par concepts peut être modifiée. Ne pas oublier de modifier le paramètre `ontologyFileLocation` et, le cas échéant, de vider les dossiers contenant le corpus d'articles et l'index issu de l'indexation par concepts.  

## Fonctionnalités 

### `CorpusController`

#### `generate` : création d'un corpus d'abstracts à partir d'une requête PubMed
Permet d'extraite les résultats de la page HTML de résultats PubMed afin de créer un corpus de fichiers en format texte (.txt) contenant les résumés d'articles scientifiques sur le domaine d'intérêt. 

Les paramètres d'entrée sont : 
1. `term` (String) : requête, par exemple *anemia*
2. `dateRange` (String) : plage de date recherchée au format AAAA-AAAA, par exemple *2010-2023*
3. `size` (Integer) : nombre de résultats souhaité, par exemple *50*

#### `index-corpus` : indexation d'un corpus de documents
Permet d'indexer le corpus créé précédemment selon 3 niveaux : par mots, par termes et par concepts.
1. **Indexation par mots simples** : l'indexation par mots simples se fait après traitement des textes par le *StandardAnalyzer* de Lucene. 
2. **Indexation par termes** : l'indexation par termes se fait par le *WhitespaceAnalyzer* de Lucene. 
3. **Indexation par concepts** : l'indexation par concepts se fait après traitement de textes par le *OntologyAnalyzer* créé dans la classe OntologyAnalyzer. 

Paramètre d'entrée : niveau d'indexation 

### `SearcherController`

#### `simple` : recherche par mots simples dans le corpus de documents
Retourne `totalHits` qui est le nombre de résultats trouvés, puis une liste de résultats composée des informations sur les documents retournés : l'identifiant `docId`, le titre `docTitle`, le résumé `docContent `et le lien URL sur PubMed `docLink`.

#### `phrase` : recherche par termes dans le corpus de documents
Retourne `totalHits` qui est le nombre de résultats trouvés, puis une liste de résultats composée des informations sur les documents retournés : l'identifiant `docId`, le titre `docTitle`, le résumé `docContent `et le lien URL sur PubMed `docLink`.

#### `concept` : recherche par concepts dans le corpus de documents
Retourne `totalHits` qui est le nombre de résultats trouvés, puis une liste de résultats composée des informations sur les documents retournés : l'identifiant `docId`, le titre `docTitle`, le résumé `docContent `et le lien URL sur PubMed `docLink`.

## Auteurs

- Laura Arles 
- Marwa Benamor
- Lou Claeyssen Fabris

## Références

- Promotion SITIS 2023
- Cours INF 301 SITIS 2023
