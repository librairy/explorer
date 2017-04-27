/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.librairy.api.model.relations.*;
import org.librairy.api.model.resources.DocumentI;
import org.librairy.api.model.resources.SimilarityI;
import org.librairy.model.domain.resources.Document;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class DocumentRestRoute extends RestRoute {

    public DocumentRestRoute() {
        super("documents", "document");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, DocumentI.class, Document.class);
//        definition = addRelationCRUD(definition, "items", null, RelationI.class, "bundled by");
        definition = addRelationCRUD(definition, "topics", WeightI.class, DealsI.class, "dealt by");
        definition = addRelationCRUD(definition, "documents", WeightDomainI.class, SimilarI.class, "similar to");

        definition = definition.get("/{id}/similarities").description("List all similarities between this document " +
                "and others")
                .outTypeList(SimilarityI.class).produces("application/json")
                //TODO domain should be a parameter
                .to("bean:documentService?method=listSimilarities(${header.id},default)");

        return definition;


    }


}
