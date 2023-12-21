package fr.bordeaux.isped.sitis.projetMoteurRecherche.abstractExtractor;

import fr.bordeaux.isped.sitis.projetMoteurRecherche.Parameters;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AbstractsLoader {


    public static void extractor(String termToExtractFrom, String dateRange, String size) {
        try {
            // Construisez l'URL de recherche avec la plage de dates.
            String pubMedUrl = "https://pubmed.ncbi.nlm.nih.gov/?term=" + termToExtractFrom + "&sort=date&size=" + size + "&filter=simsearch1.fha&filter=years." + dateRange;

            // Créez un répertoire pour stocker les résumés.
            Files.createDirectories(Paths.get(Parameters.directoryPath));

            // Effectuez une requête HTTP pour récupérer la page de résultats de PubMed.
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(pubMedUrl);
            HttpResponse response = httpClient.execute(httpGet);

            // Analysez la page HTML des résultats.
            Document document = Jsoup.parse(response.getEntity().getContent(), "UTF-8", pubMedUrl);

            // Sélectionnez la signature des individuels.
            Elements articleLinks = document.select("a.docsum-title");


            int articleCount = 0; // Compteur d'articles extraits.

            // Parcourez les liens et extrayez les résumés des articles.
            for (Element articleLink : articleLinks) {


                String articleUrl = "https://pubmed.ncbi.nlm.nih.gov" + articleLink.attr("href");


                // Visitez l'URL de l'article pour extraire le résumé.
                Document articleDocument = Jsoup.connect(articleUrl).get();
                Element abstractElement = articleDocument.select(".abstract-content").first();
                if (abstractElement != null) {
                    String abstractText = abstractElement.text();
                    //Elements articleId = document.select("a.data-article-id");

                    // Enregistrez le résumé dans un fichier texte.
                    // String fileName = "article_" + articleCount + ".txt";

                    String fileName = articleLink.text() ;
                    //String fileName = articleId.text();

                    String filePath = Paths.get(Parameters.directoryPath, fileName).toString();
                    try (FileWriter writer = new FileWriter(filePath)) {

                        writer.write(articleUrl +  '\n' + abstractText);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Résumé de l'article " + articleCount + " enregistré : " + filePath);
                }
                articleCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
