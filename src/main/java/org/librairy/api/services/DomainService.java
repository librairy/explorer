package org.librairy.api.services;

import org.librairy.api.model.relations.RelationI;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Domain;
import org.librairy.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class DomainService extends AbstractResourceService<Domain> {

    public DomainService() {
        super(Resource.Type.DOMAIN);
    }


    public void add(String sId, String rId) {
        udm.save(Relation.newContains(uriGenerator.from(Resource.Type.DOMAIN, sId), uriGenerator.from(Resource.Type.DOCUMENT, rId)));
    }

    // CONTAINS -> Document
    public List<String> listDocuments(String id) {
        String uri = uriGenerator.from(Resource.Type.DOMAIN, id);
        return udm.find(Resource.Type.DOCUMENT).from(Resource.Type.DOMAIN, uri).stream().map(res->res.getUri()).collect(Collectors.toList());
    }

    public void removeDocuments(String id) {
        String uri = uriGenerator.from(Resource.Type.DOMAIN, id);

        udm.find(Relation.Type.CONTAINS).from(Resource.Type.DOMAIN, uri)
                .forEach(rel -> udm.delete(Relation.Type.CONTAINS).byUri(rel.getUri()));
    }

    public RelationI getDocuments(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.DOMAIN, startId);
        String endUri = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        Optional<RelationI> result = udm.find(Relation.Type.CONTAINS).btw(startUri, endUri).stream().map(relation -> new RelationI(relation.getUri(), relation.getCreationTime())).findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public void addDocuments(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.DOMAIN, startId);
        String endUri = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        udm.save(Relation.newContains(startUri, endUri));
    }

    public void removeDocuments(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.DOMAIN, startId);
        String iuri = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        udm.find(Relation.Type.CONTAINS).btw(duri, iuri).forEach(relation -> udm.delete(Relation.Type.CONTAINS).byUri
                (relation.getUri()));
    }


}
