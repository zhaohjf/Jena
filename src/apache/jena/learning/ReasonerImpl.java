package apache.jena.learning;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.vocabulary.ReasonerVocabulary;

import java.util.List;

/**
 * Created by zhaohongjie on 2016/12/25.
 */
public class ReasonerImpl implements IReasoner {

    private InfModel inf;

    public InfModel getInfModel(String ontPath, String rulePath) {
        this.inf = ModelFactory.createInfModel(getReasoner(rulePath), getOntModel(ontPath));
        return this.inf;
    }

    public InfModel getInfModel(String rulePath, OntModel model) {
        this.inf = ModelFactory.createInfModel(getReasoner(rulePath), model);
		return this.inf;
    }

    private GenericRuleReasoner getReasoner(String path) {
        List<Rule> rules = Rule.rulesFromURL(path);
        GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
        reasoner.setOWLTranslation(true);
        reasoner.setDerivationLogging(true);
        reasoner.setTransitiveClosureCaching(true);
        return reasoner;
    }

    private OntModel getOntModel(String path) {
        Model model = ModelFactory.createDefaultModel();
        model.read(path);
        OntModel ont = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, model);
        Resource configuration = ont.createResource();
        configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");
        return ont;
    }

    public void printInferResult(Resource a, Resource b) {
        StmtIterator stmtIter = this.inf.listStatements(a, null, b);
		if (!stmtIter.hasNext()) {
			System.out.println("there is no relation between "
				      + a.getLocalName() + " and " + b.getLocalName());
			System.out.println("\n-------------------\n");
		}
		while (stmtIter.hasNext()) {
			Statement s = stmtIter.nextStatement();
			System.out.println("Relation between " + a.getLocalName() + " and "
				      + b.getLocalName() + " is :");
			System.out.println(a.getLocalName() + " "
				      + s.getPredicate().getLocalName() + " " + b.getLocalName());
			System.out.println("\n-------------------\n");
		}
    }

    public void searchOnto(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, this.inf);
        ResultSet results = qe.execSelect();
        ResultSetFormatter.out(System.out, results, query);
        qe.close();
    }
}
