/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.api.services.DocumentService;
import org.librairy.api.services.SourceService;
import org.librairy.model.domain.resources.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 18/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "librairy.columndb.host       = wiener.dia.fi.upm.es",
        "librairy.documentdb.host   = wiener.dia.fi.upm.es",
        "librairy.graphdb.host           = wiener.dia.fi.upm.es",
        "librairy.eventbus.host                 = wiener.dia.fi.upm.es",
        "librairy.uri = drinventor.eu"
})
public class DeployTest {

    private static final Logger LOG = LoggerFactory.getLogger(DeployTest.class);


    @Autowired
    DocumentService service;

    @Test
    public void deploy() throws InterruptedException {
        Assert.assertTrue(true);

        LOG.info("Sleeping...");

        service.listSimilarities("0284bdb8_3cf5_404c_b25a_7bd02f8e5684","default").forEach(similarity -> LOG.info
                ("Similarity: " + similarity));

        Thread.sleep(3000000);

    }
}
