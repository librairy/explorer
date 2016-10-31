/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.librairy.model.domain.resources.Filter;
import org.librairy.model.domain.resources.Resource;
import org.librairy.modeler.lda.builder.TopicsDistributionBuilder;
import org.librairy.modeler.lda.models.Text;
import org.librairy.modeler.lda.models.TopicsDistribution;
import org.librairy.storage.UDM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class InferenceCache {

    private static final Logger LOG = LoggerFactory.getLogger(InferenceCache.class);

    @Autowired
    UDM udm;

    @Autowired
    TopicsDistributionBuilder topicsDistributionBuilder;

    private LoadingCache<InferenceKey, TopicsDistribution> cache;

    @PostConstruct
    public void setup(){
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<InferenceKey, TopicsDistribution>() {
                            public TopicsDistribution load(InferenceKey key) {
                                // read filter
                                Optional<Resource> resource = udm.read(Resource.Type.FILTER).byUri(key.getFilterUri());

                                if (!resource.isPresent()) throw new RuntimeException("Filter not found: " + key);

                                Filter filter = resource.get().asFilter();

                                // Compose a given text
                                Text text = new Text();
                                text.setId("given-text");
                                text.setContent(filter.getContent());

                                return topicsDistributionBuilder.inference(key.getDomainUri(), Arrays.asList(new
                                        Text[]{text})).get(0);
                            }
                        });
    }


    public TopicsDistribution inference(String filterUri, String domainUri){
        try {
            return this.cache.get(new InferenceKey(filterUri,domainUri));
        } catch (ExecutionException e) {
            LOG.error("Error getting inference from filter: " + filterUri + " in domain: " + domainUri, e);
            return new TopicsDistribution();
        }
    }

}
