package fr.bordeaux.isped.sitis.projetMoteurRecherche.indexerSearcher;

import fr.bordeaux.isped.sitis.projetMoteurRecherche.OntologyAnalyzer;
import fr.bordeaux.isped.sitis.projetMoteurRecherche.Parameters;
import fr.bordeaux.isped.sitis.projetMoteurRecherche.domain.DocumentDomain;
import fr.bordeaux.isped.sitis.projetMoteurRecherche.ontologyLoader.Ontology;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Searcher {

    public static Analyzer myAnalyzer;

    public static String indexToUse;
    public static List<DocumentDomain> search(Integer searchLevel, String queryInput) throws IOException, ParseException {

        List<DocumentDomain> myDocuments = new ArrayList<>() ;


        if(searchLevel== Parameters.CONCEPTS)  {
            indexToUse = Parameters.indexForOntoLocation;
            myAnalyzer= new StandardAnalyzer();
            Set<String> relatedTerms = new Ontology(Parameters.ontologyFileLocation).getRelatedTerms(queryInput);
            for(String relatedTerm : relatedTerms)  {
                queryInput +=  " OR " + relatedTerm;
            }

            System.out.println(queryInput);
        } else if (searchLevel==Parameters.PHRASE) {
            myAnalyzer = new WhitespaceAnalyzer();
            indexToUse = Parameters.indexForPhrases;
            queryInput = "\"" +queryInput+  "\"";

        } else {
            myAnalyzer = new OntologyAnalyzer();
            indexToUse = Parameters.indexLocation;

            System.out.println("Voici le terme de la requete " + queryInput);
        }

        //On récupère le dossier ou se trouve l'index et on crée le searcher
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexToUse).toPath()));
        IndexSearcher searcher = new IndexSearcher(reader);



        TopScoreDocCollector collector = TopScoreDocCollector.create(10, 100);
        Query query = new QueryParser("contents", myAnalyzer).parse(queryInput);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        System.out.println("Trouvé " + hits.length + " hits.");

        for (int i = 0; i < hits.length; ++i){

            int docId = hits[i].doc;
            Document document = searcher.doc(docId);

            //Je crée un MyDocument
            DocumentDomain myDocument= new DocumentDomain();

            //Ajout Id
            myDocument.setDocId(docId);
            System.out.println(document);
            // System.out.println((i + 1) + ". " + document.get("path")  +" score=" + hits[i].score);
            // System.out.println(document.get("filename"));


            //ajout titre
            myDocument.setDocTitle(document.get("filename"));

            Path filePath = Paths.get(document.get("path"));

            //On retire la première qui correspondait au lien

            String fileContent = Files.readString(filePath);

            int index = fileContent.indexOf("\n");
            if (index != -1) {
                fileContent = fileContent.substring(index + 1); // +1 pour exclure le saut de ligne
            } else {
                fileContent = ""; // Si aucune nouvelle ligne n'est trouvée, la chaîne est vide
            }




            //Ajout contenu
            myDocument.setDocContent(fileContent);

            try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {


                String firstLine = br.readLine();
                myDocument.setDocLink(firstLine);

            } catch (Exception e) {
                e.printStackTrace();
            }



            Terms terms = reader.getTermVector(docId, "contents");
            TermsEnum termsEnum = terms.iterator();

            while (termsEnum.next() != null) {
                String term = termsEnum.term().utf8ToString();
                // System.out.println("Term: " + term);
            }

            System.out.println("Voici le document" + (i+1) );
            System.out.println(myDocument.getDocId());
            System.out.println(myDocument.getDocTitle());
            System.out.println(myDocument.getDocContent());
            System.out.println(myDocument.getDocLink());
            myDocuments.add(myDocument);
        }

        return myDocuments;

    }


}
