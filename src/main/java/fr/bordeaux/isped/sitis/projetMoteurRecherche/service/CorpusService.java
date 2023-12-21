package fr.bordeaux.isped.sitis.projetMoteurRecherche.service;

import fr.bordeaux.isped.sitis.projetMoteurRecherche.abstractExtractor.AbstractsLoader;
import fr.bordeaux.isped.sitis.projetMoteurRecherche.indexerSearcher.Indexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CorpusService {

    @Autowired
    private Indexer corpusIndexer;

    public void generateCorpus(String termToExtractFrom, String dateRange, String size) throws IOException {
        AbstractsLoader.extractor(termToExtractFrom, dateRange, size);
        // Attention dateRange à pour forme 2023-2024 si on veut les articles entre ces années.
    }

    public void indexCorpus(Integer indexLevel) {
        try {
            corpusIndexer.indexer(indexLevel);
        } catch (IOException e) {
            e.printStackTrace(); // Vous pouvez gérer les exceptions de manière appropriée ici
        }
    }

}
