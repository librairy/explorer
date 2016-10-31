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
import org.librairy.model.domain.resources.Filter;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class FilterRestRoute extends RestRoute {

    public FilterRestRoute() {
        super("filters", "filter");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, FilterI.class, Filter.class);

        // related topics
        definition = definition.get("/{id}/topics")
                .description("List topics for the given filter")
                .outTypeList(WeightResourceI.class)
                .produces("application/json")
                .to("bean:filterService?method=listTopics(${header.id})");


        // similar documents
        definition = definition.get("/{id}/similar/documents")
                .description("List top similar documents for the given filter")
                .outTypeList(WeightResourceI.class)
                .produces("application/json")
                .to("bean:filterService?method=listSimilarDocuments(${header.id})");

        // similar items
        definition = definition.get("/{id}/similar/items")
                .description("List top the similar items for the given filter")
                .outTypeList(WeightResourceI.class)
                .produces("application/json")
                .to("bean:filterService?method=listSimilarItems(${header.id})");

        // similar parts
        definition = definition.get("/{id}/similar/parts")
                .description("List top the similar parts for the given filter")
                .outTypeList(WeightResourceI.class)
                .produces("application/json")
                .to("bean:filterService?method=listSimilarParts(${header.id})");

        // matched documents
        definition = definition.get("/{id}/matches/documents")
                .description("List top matched documents for the given filter")
                .outTypeList(WeightResourceI.class)
                .produces("application/json")
                .to("bean:matchesService?method=listMatchedDocuments(${header.id})");

        // matched items
        definition = definition.get("/{id}/matches/items")
                .description("List top matched items for the given filter")
                .outTypeList(WeightResourceI.class)
                .produces("application/json")
                .to("bean:matchesService?method=listMatchedItems(${header.id})");

        // matched parts
        definition = definition.get("/{id}/matches/parts")
                .description("List top matched parts for the given filter")
                .outTypeList(WeightResourceI.class)
                .produces("application/json")
                .to("bean:matchesService?method=listMatchedParts(${header.id})");

        // matched resources
        definition = definition.get("/{id}/matches")
                .description("List top matched resources for the given filter")
                .outTypeList(WeightResourceI.class)
                .produces("application/json")
                .to("bean:matchesService?method=listMatches(${header.id})");

        return definition;
    }


}
