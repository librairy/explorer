package org.librairy.api;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.api.services.SourceService;
import org.librairy.model.domain.resources.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 18/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class DeployTest {

    private static final Logger LOG = LoggerFactory.getLogger(DeployTest.class);


    @Autowired
    SourceService sourceService;

    @Test
    public void deploy() throws InterruptedException {
        Assert.assertTrue(true);


    }
}
