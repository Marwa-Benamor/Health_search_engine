package fr.bordeaux.isped.sitis.projetMoteurRecherche.indexerSearcher;

import org.apache.lucene.analysis.CharArraySet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BuildCharArraySet {
    public static CharArraySet buildStopWord(String chemin) {
        ArrayList<String> stopWord = new ArrayList<>();
        try {
            File f = new File(chemin);
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {

                stopWord.add(line);
            }


        } catch (IOException i) {

        }
        return new CharArraySet(stopWord, false);
    }
}
