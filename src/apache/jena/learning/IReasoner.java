package apache.jena.learning;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Resource;

/**
 * Created by zhaohongjie on 2016/12/25.
 */
public interface IReasoner {

    public InfModel getInfModel(String ontPath, String rulePath);
	public InfModel getInfModel(String rulePath, OntModel model);
    public void printInferResult(Resource a, Resource b);
	public void searchOnto(String queryString);
}

