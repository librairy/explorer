/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.librairy.api.model.relations.WeightResourceI;
import org.librairy.api.model.resources.FilterI;
import org.librairy.api.model.resources.PathI;
import org.librairy.model.domain.resources.Filter;
import org.librairy.model.domain.resources.Path;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class PathRestRoute extends RestRoute {

    public PathRestRoute() {
        super("paths", "path");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, PathI.class, Path.class);

        // similar documents
        definition = definition.get("/{id}/documents")
                .description("Reading path based on Documents to move from the 'start' to the 'end' resource")
                .outTypeList(WeightResourceI.class)
                .produces("application/json")
                .to("bean:pathService?method=listDocuments(${header.id})");

        // similar items
        definition = definition.get("/{id}/items")
                .description("Reading path based on Items to move from the 'start' to the 'end' resource")
                .outTypeList(WeightResourceI.class)
                .produces("application/json")
                .to("bean:pathService?method=listItems(${header.id})");

        // similar parts
        definition = definition.get("/{id}/parts")
                .description("Reading path based on Parts to move from the 'start' to the 'end' resource")
                .outTypeList(WeightResourceI.class)
                .produces("application/json")
                .to("bean:pathService?method=listParts(${header.id})");

        return definition;
    }


}
