/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.librairy.api.model.relations.DealsI;
import org.librairy.api.model.relations.SimilarI;
import org.librairy.api.model.relations.WeightDomainI;
import org.librairy.api.model.relations.WeightI;
import org.librairy.api.model.resources.ItemI;
import org.librairy.model.domain.resources.Item;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class ItemRestRoute extends RestRoute {

    public ItemRestRoute() {
        super("items", "item");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, ItemI.class, Item.class);
        definition = addRelationCRUD(definition, "topics", WeightI.class, DealsI.class, "dealt by");
        definition = addRelationCRUD(definition, "items", WeightDomainI.class, SimilarI.class, "similar to");
        return definition;


    }


}
