package fr.bordeaux.isped.sitis.projetMoteurRecherche.indexerSearcher;

import fr.bordeaux.isped.sitis.projetMoteurRecherche.OntologyAnalyzer;
import fr.bordeaux.isped.sitis.projetMoteurRecherche.Parameters;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
@Component
public class Indexer {
    public static ArrayList<File> myCorpusFiles = new ArrayList<File>();

    public   static Analyzer myAnalyzer;

    public static String indexToUse;
    public static void indexer(Integer indexLevel) throws IOException {
        if(indexLevel== Parameters.CONCEPTS)  {
            myAnalyzer= new StandardAnalyzer();
            indexToUse = Parameters.indexForOntoLocation;
        } else if (indexLevel==Parameters.PHRASE) {
            myAnalyzer = new WhitespaceAnalyzer();
            indexToUse = Parameters.indexForPhrases;
        } else {
            myAnalyzer = new OntologyAnalyzer();
            indexToUse = Parameters.indexLocation;
        }


        // On crée l'indexWriter prêt à indexer les fichiers
        FSDirectory dir = FSDirectory.open(new File(indexToUse).toPath());
        IndexWriterConfig config = new IndexWriterConfig(myAnalyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);

        //-------------------------------------------------------------------------------

        // On charge tous les fichiers du corpus dans myCorpusFiles, avant de le traiter ensuite
        File directory = new File(Parameters.directoryPath);
        if (directory.isDirectory()) {
            File[] filesInDirectory = directory.listFiles();

            if (filesInDirectory != null) {
                for (File file : filesInDirectory) {
                    if (file.isFile()) {
                        myCorpusFiles.add(file);
                    }
                }
            } else {
                System.out.println("Le dossier est vide ou inaccessible.");
            }
        } else {
            System.out.println("Le chemin spécifié n'est pas un dossier valide.");
        }

        //--------------------------------------------------------------------------------

        // Ensuite, au sein d'une boucle, on indexe chaque fichier du corpus en le parcourant

        for (File activeFile : myCorpusFiles  )  {

            Document doc = new Document();

            FileReader fr = new FileReader(activeFile);
            // doc.add(new TextField("contents", fr));

            FieldType fieldtype = new
                    FieldType(TextField.TYPE_NOT_STORED);

            fieldtype.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
            fieldtype.setStoreTermVectors(true);
            fieldtype.setStoreTermVectorOffsets(true);
            fieldtype.setStoreTermVectorPayloads(true);
            fieldtype.setStoreTermVectorPositions(true);
            fieldtype.setTokenized(true);
            //
            // doc.add();
            // Field ff = new Field("contents", fr, fieldtype);

            Field ff = new Field("contents", activeFile.getName(), fieldtype);
            //  doc.add(new TextField("contents", fr));
            doc.add(ff);
            doc.add(new StringField("path", activeFile.getPath(), Field.Store.YES));
            doc.add(new StringField("filename", activeFile.getName(),
                    Field.Store.YES));

            indexWriter.addDocument(doc);
            System.out.println("Ajouté: " + activeFile);

        }
        indexWriter.close();

    }



}
