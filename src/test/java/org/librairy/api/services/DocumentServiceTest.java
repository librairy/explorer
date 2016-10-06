/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.services;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.api.Config;
import org.librairy.api.model.resources.SimilarityI;
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
        "librairy.columndb.port                = 5011",
        "librairy.documentdb.host   = wiener.dia.fi.upm.es",
        "librairy.documentdb.port            = 5021",
        "librairy.graphdb.host           = wiener.dia.fi.upm.es",
        "librairy.graphdb.port                    = 5030",
        "librairy.eventbus.host                 = wiener.dia.fi.upm.es",
        "librairy.eventbus.port                 = 5041",
        "librairy.uri = drinventor.eu"
})
public class DocumentServiceTest {

    @Autowired
    DocumentService documentService;

    @Test
    public void similars(){
        String id = "a94d83449b3cca72e3299109b5b53b01";

        List<String> docs = documentService.listDocuments(id);

        System.out.println(docs);

    }

    @Test
    public void similarities(){
        String id = "39d9c4c0bb38b5fd740be63ad4cbb82c";

        List<SimilarityI> sims = documentService.listSimilarities(id);

        System.out.println(sims);

    }
}
