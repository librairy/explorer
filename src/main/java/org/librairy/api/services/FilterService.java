/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.services;

import org.librairy.api.cache.InferenceCache;
import org.librairy.api.model.relations.WeightResourceI;
import org.librairy.api.model.resources.SimilarityI;
import org.librairy.api.model.resources.TopicDistributionI;
import org.librairy.api.model.resources.WordDistributionI;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Filter;
import org.librairy.model.domain.resources.Resource;
import org.librairy.modeler.lda.builder.SimilarityBuilder;
import org.librairy.modeler.lda.builder.TopicsDistributionBuilder;
import org.librairy.modeler.lda.models.TopicsDistribution;
import org.librairy.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class FilterService extends AbstractResourceService<Filter> {

    @Autowired
    TopicsDistributionBuilder topicsDistributionBuilder;

    @Autowired
    SimilarityBuilder similarityBuilder;

    @Autowired
    InferenceCache cache;

    //TODO Update Camel to 2.17.x to define this as query param
    String domainId = "default";

    //TODO Update Camel to 2.17.x to define this as query param
    String n = "20";


    private static final Logger LOG = LoggerFactory.getLogger(FilterService.class);

    public FilterService() {
        super(Resource.Type.FILTER);
    }


    public List<WeightResourceI> listSimilarDocuments(String id) throws IOException, ClassNotFoundException {
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        String dUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);
        Integer top = Integer.valueOf(n);
        return findSimilar(uri, dUri, top);
    }


    public List<WeightResourceI> listSimilarItems(String id) throws IOException, ClassNotFoundException {
        //TODO Handle Items
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        String dUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);
        Integer top = Integer.valueOf(n);
        return findSimilar(uri, dUri, top);
    }


    public List<WeightResourceI> listSimilarParts(String id) throws IOException, ClassNotFoundException {
        //TODO Handle Parts
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        String dUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);
        Integer top = Integer.valueOf(n);
        return findSimilar(uri, dUri, top);
    }


    public List<WeightResourceI> listSimilar(String id) throws IOException, ClassNotFoundException {
        //TODO Handle All the types
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        String dUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);
        Integer top = Integer.valueOf(n);
        return findSimilar(uri, dUri, top);
    }


    private List<WeightResourceI> findSimilar(String filterUri, String domainUri, int top) throws IOException,
            ClassNotFoundException {

        // Getting topics distribution for the given text
        TopicsDistribution inferencedTopics = cache.inference(filterUri, domainUri);

        // Getting similar items based on that topics distribution
        return similarityBuilder.topSimilars(Resource.Type.DOCUMENT, domainUri, top, inferencedTopics.getTopics())
                .stream()
                .map(similarResource -> {
                    WeightResourceI resource = new WeightResourceI();
                    resource.setResource(similarResource.getUri());
                    resource.setWeight(similarResource.getWeight());
                    String title = udm.read(Resource.Type.DOCUMENT).byUri(similarResource.getUri()).get().asDocument
                            ().getTitle();
                    resource.setDescription(title);
                    return resource;
                })
                .collect(Collectors.toList())
        ;
    }


    public List<TopicDistributionI> listTopics(String id){
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        String dUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);
        return cache.inference(uri, dUri).getTopics().stream().sorted((o1,o2) -> -o1.getWeight().compareTo(o2
                .getWeight())).map
                (td -> {
            TopicDistributionI tdi = new TopicDistributionI();
            tdi.setTopic(td.getTopicUri());
            tdi.setRelevance(td.getWeight());

            List<WordDistributionI> words = udm.find(Relation.Type.MENTIONS_FROM_TOPIC)
                    .from(Resource.Type.TOPIC,td.getTopicUri())
                    .stream()
                    .map(rel ->
                            new WordDistributionI(udm.read(Resource.Type.WORD).byUri(rel.getEndUri()).get().asWord()
                                    .getContent(),rel
                                    .getWeight()))
                    .collect(Collectors.toList());
            tdi.setWords(words);
            return tdi;
        })
                .collect(Collectors.toList());
    }

}
