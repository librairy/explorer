/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.services;

import org.librairy.api.model.relations.WeightResourceI;
import org.librairy.model.domain.resources.Filter;
import org.librairy.model.domain.resources.Resource;
import org.librairy.modeler.lda.builder.SimilarityBuilder;
import org.librairy.modeler.lda.builder.TopicsDistributionBuilder;
import org.librairy.modeler.lda.models.Text;
import org.librairy.modeler.lda.models.TopicsDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
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
        Filter filter = udm.read(Resource.Type.FILTER).byUri(uri).get().asFilter();
        return findSimilar(filter.getContent(), dUri, top);
    }


    public List<WeightResourceI> listSimilarItems(String id) throws IOException, ClassNotFoundException {
        //TODO Handle Items
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        String dUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);
        Integer top = Integer.valueOf(n);
        Filter filter = udm.read(Resource.Type.FILTER).byUri(uri).get().asFilter();
        return findSimilar(filter.getContent(), dUri, top);
    }


    public List<WeightResourceI> listSimilarParts(String id) throws IOException, ClassNotFoundException {
        //TODO Handle Parts
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        String dUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);
        Integer top = Integer.valueOf(n);
        Filter filter = udm.read(Resource.Type.FILTER).byUri(uri).get().asFilter();
        return findSimilar(filter.getContent(), dUri, top);
    }


    public List<WeightResourceI> listSimilar(String id) throws IOException, ClassNotFoundException {
        //TODO Handle All the types
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        String dUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);
        Integer top = Integer.valueOf(n);
        Filter filter = udm.read(Resource.Type.FILTER).byUri(uri).get().asFilter();
        return findSimilar(filter.getContent(), dUri, top);
    }


    private List<WeightResourceI> findSimilar(String inputText, String domainUri, int top) throws IOException,
            ClassNotFoundException {

        // Compose a given text
        Text text = new Text();
        text.setId("given-text");
        text.setContent(inputText);

        // Getting topics distribution for the given text
        List<TopicsDistribution> inferencedTopics = topicsDistributionBuilder.inference(domainUri, Arrays.asList(new
                Text[]{text}));

        // Getting similar items based on that topics distribution
        return similarityBuilder.topSimilars(Resource.Type.DOCUMENT, domainUri, top, inferencedTopics.get(0).getTopics())
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



}
