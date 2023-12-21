package fr.bordeaux.isped.sitis.projetMoteurRecherche.ontologyLoader;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class Ontology {

    private OntModel ontologyModel;

    // Constructeur de la classe Ontology
    public Ontology(String ontologyFilePath) {
        // Initialisation du modèle d'ontologie à partir du fichier OWL spécifié
        this.ontologyModel = ModelFactory.createOntologyModel();
        InputStream in = FileManager.get().open(ontologyFilePath);
        if (in == null) {
            throw new IllegalArgumentException("Fichier d'ontologie introuvable");
        }
        ontologyModel.read(in, null);
    }

    // Méthode pour récupérer les termes associés à un terme spécifique dans l'ontologie
    public Set<String> getRelatedTerms(String term) {
        Set<String> relatedTerms = new HashSet<>();

        // Supposons que chaque terme est une classe dans l'ontologie
        OntClass concept = ontologyModel.getOntClass(getURIFromLabel(term));
        if (concept != null) {
            // Obtenez les sous-classes (enfants) du concept pour récupérer les termes associés
            for (OntClass subClass : concept.listSubClasses().toSet()) {

                // Récupération des libellés avec rdfs:label
                StmtIterator labelStmts = subClass.listProperties(ontologyModel.getProperty("http://www.w3.org/2000/01/rdf-schema#label"));
                // System.out.println(labelStmts);
                while (labelStmts.hasNext()) {
                    Statement labelStmt = labelStmts.next();
                    if (labelStmt.getObject().isLiteral()) {

                        String label = labelStmt.getLiteral().getString();
                        // System.out.println( label);

                        relatedTerms.add(label);

                    }
                }








                //relatedTerms.add(subClass.getLocalName());
                // Vous pouvez ajouter plus de logique ici pour explorer d'autres relations dans l'ontologie
            }
        } else {
            System.out.println("vide");
            return null;
        }
        System.out.println("lide retrouvée");
        return relatedTerms;
    }


    public String getURIFromLabel(String label)   {
        String foundURI = null;
        String sparqlQueryString =
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                        "SELECT ?uri " +
                        "WHERE {" +
                        "   ?uri rdfs:label \"" + label + "\"." +
                        "}";
        Query query = QueryFactory.create(sparqlQueryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, ontologyModel)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode uriNode = soln.get("uri");
                if (uriNode.isResource()) {
                    foundURI = uriNode.asResource().getURI();
                    // System.out.println("URI trouvé pour le libellé '" + label+ "': " + foundURI);
                    // Utilisez foundURI selon vos besoins
                }
            }
        }
        System.out.println(foundURI);
        return foundURI;
    }
    // Autres méthodes pour travailler avec votre ontologie

}
