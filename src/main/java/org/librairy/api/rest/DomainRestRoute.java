package org.librairy.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.librairy.api.model.relations.RelationI;
import org.librairy.api.model.resources.DomainI;
import org.librairy.model.domain.resources.Domain;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class DomainRestRoute extends RestRoute {

    public DomainRestRoute() {
        super("domains", "domain");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, DomainI.class, Domain.class);
        definition = addRelationCRUD(definition, "documents", null, RelationI.class, "contained in");
        return definition;

    }
}
