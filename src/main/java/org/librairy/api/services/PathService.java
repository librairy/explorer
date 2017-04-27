/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.services;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.librairy.api.model.relations.WeightResourceI;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Filter;
import org.librairy.model.domain.resources.Path;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.system.graph.template.TemplateExecutor;
import org.librairy.storage.system.graph.template.edges.SimilarDocEdgeTemplate;
import org.librairy.storage.system.graph.template.nodes.DocumentNodeTemplate;
import org.neo4j.ogm.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class PathService extends AbstractResourceService<Filter> {

    @Autowired
    TemplateExecutor executor;

    @Autowired
    SimilarDocEdgeTemplate similarDocEdgeTemplate;

    @Autowired
    EnricherService enricherService;

    Double minSimilarValue = 0.7;

    private static final Logger LOG = LoggerFactory.getLogger(PathService.class);

    public PathService() {
        super(Resource.Type.PATH);
    }


    public List<WeightResourceI> listDocuments(String id) throws IOException, ClassNotFoundException {
        return listResources(id, "Document");
    }

    public List<WeightResourceI> listItems(String id) throws IOException, ClassNotFoundException {
        return listResources(id, "Item");
    }

    public List<WeightResourceI> listParts(String id) throws IOException, ClassNotFoundException {
        return listResources(id, "Part");
    }

    private List<WeightResourceI> listResources(String id, String type) throws IOException, ClassNotFoundException {

        List<WeightResourceI> resources = new ArrayList<>();

        String uri = uriGenerator.from(Resource.Type.PATH, id);

        Path path = udm.read(Resource.Type.PATH).byUri(uri).get().asPath();

        String query = "match (s : "+type+" { uri : {0} }), (e : "+type+" { uri : {1} }), path = shortestPath ((s)" +
                "-[r:SIMILAR_TO*1..10]-(e)) WHERE ALL (r in rels(path) WHERE r.weight > "+minSimilarValue+") return path";


        Optional<Result> results = executor.query(query, ImmutableMap.of("0",path.getStart(),"1",path.getEnd()));

        if (!results.isPresent()) return resources;


        Iterator<Map<String, Object>> iterator = results.get().queryResults().iterator();

        if(iterator.hasNext()){
            Map<String, Object> resource = iterator.next();
            Map resultPath = (Map) resource.get("path");
            List<String>  relationships = (List<String>) resultPath.get("relationships");
            List<String>  directions = (List<String>) resultPath.get("directions");
            LOG.info("Relations: " + relationships);

            WeightResourceI startResource = new WeightResourceI();
            startResource.setResource(path.getStart());
            startResource.setWeight(0.0);
            startResource.setDescription(enricherService.composeDescriptionFrom(path.getStart()));
            resources.add(startResource);

            int index = 0;
            for (String reference: relationships){

                Long relId = Long.valueOf(StringUtils.substringAfterLast(reference,"/"));
                Relation relation = similarDocEdgeTemplate.fromNodeId(relId).get();

                String direction = directions.get(index++);
                String refUri = direction.equals("->")? relation.getEndUri() : relation.getStartUri();

                WeightResourceI weightResourceI = new WeightResourceI();
                weightResourceI.setResource(refUri);
                weightResourceI.setWeight(relation.getWeight());
                weightResourceI.setDescription(enricherService.composeDescriptionFrom(refUri));

                resources.add(weightResourceI);
            }
        }

        return resources;
    }

}
