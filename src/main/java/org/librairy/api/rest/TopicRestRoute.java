/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.librairy.api.model.relations.AnalysisI;
import org.librairy.api.model.relations.EmergesI;
import org.librairy.api.model.relations.MentionsI;
import org.librairy.api.model.relations.WeightTimesI;
import org.librairy.api.model.resources.TopicI;
import org.librairy.model.domain.resources.Topic;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 25/02/16.
 */
@Component
public class TopicRestRoute extends RestRoute {

    public TopicRestRoute() {
        super("topics", "topic");
    }

    @Override
    public RestDefinition configure(RestsDefinition definitions) {

        RestDefinition definition = addResourceCRUD(definitions, TopicI.class, Topic.class);
        definition = addRelationCRUD(definition, "domains", AnalysisI.class, EmergesI.class, "where emerges");
        definition = addRelationCRUD(definition, "words", WeightTimesI.class, MentionsI.class, "mentioned by");
        return definition;


    }


}
