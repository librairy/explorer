package org.librairy.api.services;

import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.librairy.api.model.relations.WeightResourceI;
import org.librairy.model.domain.resources.Filter;
import org.librairy.model.domain.resources.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class MatchesService extends AbstractResourceService<Filter> {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    EnricherService enricherService;


    private static final Logger LOG = LoggerFactory.getLogger(MatchesService.class);

    public MatchesService() {
        super(Resource.Type.FILTER);
    }


    public List<WeightResourceI> listMatchedDocuments(String id) throws IOException, ClassNotFoundException {
        return matches(id, "documents");
    }


    public List<WeightResourceI> listMatchedItems(String id) throws IOException, ClassNotFoundException {
        return matches(id, "items");
    }


    public List<WeightResourceI> listMatchedParts(String id) throws IOException, ClassNotFoundException {
        return matches(id, "parts");
    }


    public List<WeightResourceI> listMatches(String id) throws IOException, ClassNotFoundException {
        return matches(id, "documents", "items", "parts", "words", "domains", "topics");
    }

    private List<WeightResourceI> matches(String filterId, String... type){

        String uri = uriGenerator.from(Resource.Type.FILTER, filterId);
        Filter filter = udm.read(Resource.Type.FILTER).byUri(uri).get().asFilter();


        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("research")
                .withTypes(type)
                .withQuery(matchPhraseQuery("content",filter.getContent()))
                .withSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
                .build();

        ResultsExtractor<List<WeightResourceI>> result = new ResultsExtractor<List<WeightResourceI>>() {
            @Override
            public List<WeightResourceI> extract(SearchResponse searchResponse) {
                List<WeightResourceI> resultList = new ArrayList<>();
                LOG.info("Took: " + searchResponse.getTook().toString());

                SearchHits hits = searchResponse.getHits();

                LOG.info("Total Hits: " + hits.totalHits());

                for (SearchHit hit : hits.hits()){
                    WeightResourceI wresource = new WeightResourceI();
                    wresource.setResource(hit.getId());
                    wresource.setWeight(Double.valueOf(hit.getScore()));
                    resultList.add(wresource);
                }

                return resultList;
            }
        };

        return elasticsearchTemplate.query(searchQuery, result).stream()
                .map(res -> res.setDescription(enricherService.composeDescriptionFrom(res.getResource())))
                .collect(Collectors.toList());
    }

}
