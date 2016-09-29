/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.librairy.api.model.relations.RelationI;
import org.librairy.api.model.resources.SourceI;
import org.librairy.model.domain.resources.Source;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class SourceRestRoute extends RestRoute {

    private static final String SERVICE = "sourceService";

    public SourceRestRoute() {
        super("sources", "source");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, SourceI.class, Source.class);
        definition = addRelationCRUD(definition, "documents", null, RelationI.class, "provided by");
        definition = addRelationCRUD(definition, "domains", null, RelationI.class, "composed from");
        return definition;

    }
}
