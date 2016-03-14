package org.librairy.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.librairy.api.model.relations.DealsI;
import org.librairy.api.model.relations.RelationI;
import org.librairy.api.model.relations.SimilarI;
import org.librairy.api.model.relations.WeightI;
import org.librairy.api.model.resources.PartI;
import org.librairy.model.domain.resources.Part;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class PartRestRoute extends RestRoute {

    public PartRestRoute() {
        super("parts", "part");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, PartI.class, Part.class);
        definition = addRelationCRUD(definition, "items", null, RelationI.class, "described by");
        definition = addRelationCRUD(definition, "topics", WeightI.class, DealsI.class, "dealt by");
        definition = addRelationCRUD(definition, "parts", DealsI.class, SimilarI.class, "similar to");
        return definition;


    }


}
