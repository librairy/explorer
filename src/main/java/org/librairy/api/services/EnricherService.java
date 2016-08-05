package org.librairy.api.services;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.librairy.api.model.relations.WeightResourceI;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Filter;
import org.librairy.model.domain.resources.Path;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.UDM;
import org.librairy.storage.generator.URIGenerator;
import org.librairy.storage.system.graph.template.TemplateExecutor;
import org.librairy.storage.system.graph.template.edges.SimilarDocEdgeTemplate;
import org.neo4j.ogm.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class EnricherService {

    @Autowired
    UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    private static final Logger LOG = LoggerFactory.getLogger(EnricherService.class);

    public String composeDescriptionFrom(String uri){
        switch (uriGenerator.getResourceFrom(uri)){
            case DOCUMENT:
                return udm.read(Resource.Type.DOCUMENT).byUri(uri).get().asDocument().getTitle();
            case ITEM:
                return StringUtils.substring(udm.read(Resource.Type.ITEM).byUri(uri).get().asItem()
                        .getContent(),0,100)+"..";
            case PART:
                return StringUtils.substring(udm.read(Resource.Type.PART).byUri(uri).get().asPart()
                        .getContent(),0,100)+"..";
            case ANALYSIS:
                return udm.read(Resource.Type.ANALYSIS).byUri(uri).get().asAnalysis().getDescription();
            case DOMAIN:
                return udm.read(Resource.Type.DOMAIN).byUri(uri).get().asDomain().getName();
            case SOURCE:
                return udm.read(Resource.Type.SOURCE).byUri(uri).get().asSource().getName();
            case TERM:
                return udm.read(Resource.Type.TERM).byUri(uri).get().asTerm().getContent();
            case WORD:
                return udm.read(Resource.Type.WORD).byUri(uri).get().asWord().getContent();
            case TOPIC:
                return udm.read(Resource.Type.TOPIC).byUri(uri).get().asTopic().getContent();
            default: return "";
        }
    }

}
