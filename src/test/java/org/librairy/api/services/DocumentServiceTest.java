package org.librairy.api.services;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.api.Config;
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
        "librairy.cassandra.contactpoints       = wiener.dia.fi.upm.es",
        "librairy.cassandra.port                = 5011",
        "librairy.cassandra.keyspace            = research",
        "librairy.elasticsearch.contactpoints   = wiener.dia.fi.upm.es",
        "librairy.elasticsearch.port            = 5021",
        "librairy.neo4j.contactpoints           = wiener.dia.fi.upm.es",
        "librairy.neo4j.port                    = 5030",
        "librairy.eventbus.host                 = wiener.dia.fi.upm.es",
        "librairy.eventbus.port                 = 5041"
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
}
