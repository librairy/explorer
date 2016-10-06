/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.librairy.api.model.relations.EmbeddedI;
import org.librairy.api.model.relations.SimilarI;
import org.librairy.api.model.relations.VectorI;
import org.librairy.api.model.relations.WeightDomainI;
import org.librairy.api.model.resources.WordI;
import org.librairy.model.domain.resources.Word;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class WordRestRoute extends RestRoute {

    public WordRestRoute() {
        super("words", "word");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, WordI.class, Word.class);
        definition = addRelationCRUD(definition, "domains", VectorI.class, EmbeddedI.class, "where appears");
        definition = addRelationCRUD(definition, "words", WeightDomainI.class, SimilarI.class, "paired with");
        return definition;


    }


}
