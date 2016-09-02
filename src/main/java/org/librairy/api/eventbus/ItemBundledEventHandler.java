package org.librairy.api.eventbus;

import org.librairy.model.Event;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.modules.BindingKey;
import org.librairy.model.modules.EventBus;
import org.librairy.model.modules.EventBusSubscriber;
import org.librairy.model.modules.RoutingKey;
import org.librairy.modeler.lda.cache.CacheManager;
import org.librairy.modeler.lda.services.TopicModelingService;
import org.librairy.storage.UDM;
import org.librairy.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created on 12/07/16:
 *
 * @author cbadenes
 */
@Component
public class ItemBundledEventHandler implements EventBusSubscriber {

    private static final Logger LOG = LoggerFactory.getLogger(org.librairy.modeler.lda.eventbus.ItemBundledEventHandler.class);

    @Autowired
    protected EventBus eventBus;

    @Autowired
    protected UDM udm;

    @Value("${librairy.modeler.folder}")
    String modelFolder;

    @Value("${librairy.vocabulary.folder}")
    String vocabularyFolder;

    //TODO Update Camel to 2.17.x to define this as query param
    String domainId = "4f56ab24bb6d815a48b8968a3b157470";

    @PostConstruct
    public void init(){
        BindingKey bindingKey = BindingKey.of(RoutingKey.of(Relation.Type.BUNDLES, Relation.State.CREATED),
                "explorer-new-item");
        LOG.info("Trying to register as subscriber of '" + bindingKey + "' events ..");
        eventBus.subscribe(this,bindingKey );
        LOG.info("registered successfully");
    }

    @Override
    public void handle(Event event) {
        LOG.info("Document created event received: " + event);
        try{
            Relation relation = event.to(Relation.class);

            // BUNDLES relation
            List<Resource> domains = udm.find(Resource.Type.DOMAIN).from(Resource.Type.DOCUMENT, relation
                    .getStartUri());

            // Remove corpus serialization for these domains
            if (!domains.isEmpty()){
                domains.forEach(domain -> {
                    String id = URIGenerator.retrieveId(domain.getUri());
                    Path corpusPath = Paths.get(modelFolder,domainId,"corpus.gz");
                    try {
                        Files.deleteIfExists(corpusPath);
                        LOG.info("Deleted serialized corpus: " + corpusPath.toFile().getAbsolutePath());
                    } catch (IOException e) {
                        LOG.warn("Error deleting corpus serialization",e);
                    }
                });
            }
        } catch (Exception e){
            LOG.error("Error scheduling a new topic model for Items from domain: " + event, e);
        }
    }
}
