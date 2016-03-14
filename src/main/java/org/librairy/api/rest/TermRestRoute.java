package org.librairy.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.librairy.api.model.relations.AppearedI;
import org.librairy.api.model.relations.MentionsI;
import org.librairy.api.model.relations.ProbabilityI;
import org.librairy.api.model.relations.WeightTimesI;
import org.librairy.api.model.resources.TermI;
import org.librairy.model.domain.resources.Term;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class TermRestRoute extends RestRoute {

    public TermRestRoute() {
        super("terms", "term");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, TermI.class, Term.class);
        definition = addRelationCRUD(definition, "domains", ProbabilityI.class, AppearedI.class, "where appears");
        definition = addRelationCRUD(definition, "words", WeightTimesI.class, MentionsI.class, "mentioned by");
        return definition;


    }


}
