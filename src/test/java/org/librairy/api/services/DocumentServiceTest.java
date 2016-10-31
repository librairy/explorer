/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.services;

import com.google.common.collect.Iterables;
import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.api.Config;
import org.librairy.api.model.resources.SimilarityI;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.generator.URIGenerator;
import org.librairy.storage.system.column.repository.UnifiedColumnRepository;
import org.librairy.storage.system.graph.repository.edges.SimilarToEdgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created on 04/05/16:
 *
 * @author cbadenes
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "librairy.columndb.host       = wiener.dia.fi.upm.es",
//        "librairy.columndb.port                = 5011",
        "librairy.documentdb.host   = wiener.dia.fi.upm.es",
//        "librairy.documentdb.port            = 5021",
        "librairy.graphdb.host           = wiener.dia.fi.upm.es",
//        "librairy.graphdb.port                    = 5030",
        "librairy.eventbus.host                 = wiener.dia.fi.upm.es",
//        "librairy.eventbus.port                 = 5041",
        "librairy.topic = drinventor.eu"
})
public class DocumentServiceTest {

    @Autowired
    DocumentService documentService;

    @Autowired
    UnifiedColumnRepository columnRepository;

    @Autowired
    URIGenerator uriGenerator;

    @Test
    public void similars(){
        String id = "a94d83449b3cca72e3299109b5b53b01";

        List<String> docs = documentService.listDocuments(id);

        System.out.println(docs);

    }

    @Test
    public void similarities(){
        String id = "00106da3_eb2a_4e3c_9f1b_84b1e8c10870";

        List<SimilarityI> sims = documentService.listSimilarities(id,"default");

        System.out.println("Total:" + sims.size());

        Iterable<Relation> res1 = columnRepository.findBy(Relation.Type.SIMILAR_TO_DOCUMENTS, "start", uriGenerator
                .from(Resource.Type.DOCUMENT, id));
        System.out.println("Start: " + Iterables.size(res1));

        Iterable<Relation> res2 = columnRepository.findBy(Relation.Type.SIMILAR_TO_DOCUMENTS, "end", uriGenerator
                .from(Resource.Type
                        .DOCUMENT, id));
        System.out.println("End: " + Iterables.size(res2));
    }
}
