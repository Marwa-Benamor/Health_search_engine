package fr.bordeaux.isped.sitis.projetMoteurRecherche.controller;


import fr.bordeaux.isped.sitis.projetMoteurRecherche.Parameters;
import fr.bordeaux.isped.sitis.projetMoteurRecherche.service.SearcherService;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8083")
public class SearcherController {

    private final SearcherService searchService;

    @Autowired
    public SearcherController(SearcherService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/simple")
    public ResponseEntity<SearcherService.SearchResponse> searchBySimpleWord(@RequestParam("term") String term) {
        return search(Parameters.SIMPLE_WORDS, term);
    }

    @GetMapping("/phrase")
    public ResponseEntity<SearcherService.SearchResponse> searchByPhrase(@RequestParam("term") String term) {
        return search(Parameters.PHRASE, term);
    }

    @GetMapping("/concept")
    public ResponseEntity<SearcherService.SearchResponse> searchByConcept(@RequestParam("term") String term) {
        return search(Parameters.CONCEPTS, term);
    }

    private ResponseEntity<SearcherService.SearchResponse> search(Integer searchLevel, String term) {
        try {
            SearcherService.SearchResponse response = searchService.searchDocuments(searchLevel, term);
            return ResponseEntity.ok(response);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}