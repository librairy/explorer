package org.librairy.api.services;

import com.google.common.base.Strings;
import org.librairy.api.model.relations.RelationI;
import org.librairy.api.model.resources.SourceI;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.domain.resources.Source;
import org.librairy.model.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class SourceService extends AbstractResourceService<Source> {

    private static final Logger LOG = LoggerFactory.getLogger(SourceService.class);

    public SourceService() {
        super(Resource.Type.SOURCE);
    }


    public Source create(SourceI resource) throws Exception {

        LOG.info("Trying to create: " + resource);

        Source source = Resource.newSource();
        BeanUtils.copyProperties(resource, source);
        if (Strings.isNullOrEmpty(resource.getName())) resource.setName(resource.getUrl());
        source.setUri(uriGenerator.basedOnContent(Resource.Type.SOURCE,resource.getName()));
        source.setCreationTime(TimeUtils.asISO());
        udm.save(source);
        return source;
    }


    public Source update(String id, SourceI resource) {
        String uri = uriGenerator.from(Resource.Type.SOURCE, id);
        LOG.debug("updating by uri: " + uri);
        Optional<Resource> result = udm.read(Resource.Type.SOURCE).byUri(uri);
        if (!result.isPresent()) {
            throw new RuntimeException("Resource does not exist with uri: " + uri);
        }
        Source original = (Source) result.get();
        BeanUtils.copyProperties(resource, original);
        udm.save(original);
        return original;
    }


    // PROVIDES -> Document
    public List<String> listDocuments(String id) {
        String uri = uriGenerator.from(Resource.Type.SOURCE, id);
        return udm.find(Resource.Type.DOCUMENT).from(Resource.Type.SOURCE, uri);
    }

    public void removeDocuments(String id) {
        String uri = uriGenerator.from(Resource.Type.SOURCE, id);
        udm.delete(Relation.Type.PROVIDES).in(Resource.Type.SOURCE, uri);
    }

    public RelationI getDocuments(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.SOURCE, startId);
        String endUri = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        Optional<RelationI> result = udm.find(Relation.Type.PROVIDES).btw(startUri, endUri).stream().map(relation -> new RelationI(relation.getUri(), relation.getCreationTime())).findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public void addDocuments(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.SOURCE, startId);
        String endUri = uriGenerator.from(Resource.Type.TOPIC, endId);
        udm.save(Relation.newProvides(startUri, endUri));
    }

    public void removeDocuments(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.SOURCE, startId);
        String iuri = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        udm.find(Relation.Type.PROVIDES).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.PROVIDES).byUri(relation.getUri()));
    }

    // COMPOSES -> Document
    public List<String> listDomains(String id) {
        String uri = uriGenerator.from(Resource.Type.SOURCE, id);
        return udm.find(Resource.Type.DOMAIN).from(Resource.Type.SOURCE, uri);
    }

    public void removeDomains(String id) {
        String uri = uriGenerator.from(Resource.Type.SOURCE, id);
        udm.delete(Relation.Type.COMPOSES).in(Resource.Type.SOURCE, uri);
    }

    public RelationI getDomains(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.SOURCE, startId);
        String endUri = uriGenerator.from(Resource.Type.DOMAIN, endId);
        Optional<RelationI> result = udm.find(Relation.Type.COMPOSES).btw(startUri, endUri).stream().map(relation -> new RelationI(relation.getUri(), relation.getCreationTime())).findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public void addDomains(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.SOURCE, startId);
        String endUri = uriGenerator.from(Resource.Type.DOMAIN, endId);
        udm.save(Relation.newProvides(startUri, endUri));
    }

    public void removeDomains(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.SOURCE, startId);
        String iuri = uriGenerator.from(Resource.Type.DOMAIN, endId);
        udm.find(Relation.Type.COMPOSES).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.COMPOSES).byUri(relation.getUri()));
    }

}
