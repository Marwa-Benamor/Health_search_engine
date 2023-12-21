package fr.bordeaux.isped.sitis.projetMoteurRecherche.service;


import fr.bordeaux.isped.sitis.projetMoteurRecherche.domain.DocumentDomain;
import fr.bordeaux.isped.sitis.projetMoteurRecherche.indexerSearcher.Indexer;
import fr.bordeaux.isped.sitis.projetMoteurRecherche.indexerSearcher.Searcher;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearcherService {

    private final Indexer corpusIndexer; // Assurez-vous que vous avez un bean CorpusIndexer correctement configuré.

    @Autowired
    public SearcherService(Indexer corpusIndexer) {
        this.corpusIndexer = corpusIndexer;
    }

    public SearchResponse searchDocuments(Integer searchLevel, String queryInput) throws IOException, ParseException {
        List<DocumentDomain> documents = Searcher.search(searchLevel, queryInput);
        int totalHits = documents.size(); // Modify this to get the actual total hits
        return new SearchResponse(documents, totalHits);
    }

    public static class SearchResponse {
        private int totalHits;
        private List<DocumentDomain> documents;


        // Constructors, getters, and setters

        public SearchResponse(List<DocumentDomain> documents, int totalHits) {
            this.documents = documents;
            this.totalHits = totalHits;
        }

        public List<DocumentDomain> getDocuments() {
            return documents;
        }

        public void setDocuments(List<DocumentDomain> documents) {
            this.documents = documents;
        }

        public int getTotalHits() {
            return totalHits;
        }

        public void setTotalHits(int totalHits) {
            this.totalHits = totalHits;
        }
    }
}
