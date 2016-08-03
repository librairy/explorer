package org.librairy.api.services;

import org.librairy.api.model.resources.TermO;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.domain.resources.Term;
import org.librairy.storage.UDM;
import org.librairy.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class TermFacadeService  {

    private static final Logger LOG = LoggerFactory.getLogger(TermFacadeService.class);

    @Autowired
    protected UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    public TermO get(String id) {
        LOG.info("Handling term facade...");
        String uri = uriGenerator.from(Resource.Type.TERM, id);
        Optional<Resource> result = udm.read(Resource.Type.TERM).byUri(uri);
        if (!result.isPresent())
            return null; //TODO Handle empty result
        Term term = (Term) result.get();
        TermO tFacade = new TermO();
        tFacade.setUri(term.getUri());
        tFacade.setContent(term.getContent());
        return tFacade;
    }

}
