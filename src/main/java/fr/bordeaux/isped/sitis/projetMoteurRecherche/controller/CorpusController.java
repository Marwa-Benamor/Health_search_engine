package fr.bordeaux.isped.sitis.projetMoteurRecherche.controller;

import fr.bordeaux.isped.sitis.projetMoteurRecherche.service.CorpusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/corpus")
public class CorpusController {

    @Autowired
    private CorpusService corpusService;

    @Autowired
    public CorpusController(CorpusService corpusService) {
        this.corpusService = corpusService;
    }

    @Operation(summary = "Générer le corpus via PubMed")
    @GetMapping("/generate")
    public void generateCorpus(
            @Parameter(description = "Terme de recherche", required = true) @RequestParam("term") String term,
            @Parameter(description = "Plage de dates", required = true) @RequestParam("dateRange") String dateRange,
            @Parameter(description = "Taille du corpus", required = true) @RequestParam("size") String size)
            throws IOException {
        corpusService.generateCorpus(term, dateRange, size);
    }

    @Operation(summary = "Indexation du corpus")
    @PostMapping("/index-corpus")
    public void indexCorpus(
            @Parameter(description = "Niveau d'indexation (1 pour SIMPLE_WORDS, 2 pour PHRASE, 3 pour CONCEPTS)", required = true) @RequestParam("indexLevel") Integer indexLevel) {
        corpusService.indexCorpus(indexLevel);
    }

}
