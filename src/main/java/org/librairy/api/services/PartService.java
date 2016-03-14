package org.librairy.api.services;

import org.librairy.api.model.relations.DealsI;
import org.librairy.api.model.relations.RelationI;
import org.librairy.api.model.relations.SimilarI;
import org.librairy.api.model.relations.WeightDomainI;
import org.librairy.model.domain.relations.*;
import org.librairy.model.domain.resources.Part;
import org.librairy.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class PartService extends AbstractResourceService<Part> {

    public PartService() {
        super(Resource.Type.PART);
    }

    // SIMILAR_TO -> Part
    public List<String> listParts(String id) {
        String uri = uriGenerator.from(Resource.Type.PART, id);
        return udm.find(Resource.Type.PART).from(Resource.Type.PART, uri);
    }

    public void removeParts(String id) {
        String uri = uriGenerator.from(Resource.Type.PART, id);
        udm.delete(Relation.Type.SIMILAR_TO_PARTS).in(Resource.Type.PART, uri);
    }

    public SimilarI getParts(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.PART, startId);
        String endUri = uriGenerator.from(Resource.Type.PART, endId);
        Optional<SimilarI> result = udm.find(Relation.Type.SIMILAR_TO_PARTS).btw(startUri, endUri).stream().map(relation -> new SimilarI(relation.getUri(), relation.getCreationTime(), relation.getWeight(), ((SimilarTo) relation).getDomain())).findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public void addParts(String startId, String endId, WeightDomainI weightI) {
        String startUri = uriGenerator.from(Resource.Type.PART, startId);
        String endUri = uriGenerator.from(Resource.Type.PART, endId);
        SimilarToParts relation = Relation.newSimilarToParts(startUri, endUri);
        relation.setWeight(weightI.getWeight());
        relation.setDomain(weightI.getDomain());
        udm.save(relation);
    }

    public void removeParts(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.PART, startId);
        String iuri = uriGenerator.from(Resource.Type.PART, endId);
        udm.find(Relation.Type.SIMILAR_TO_PARTS).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.SIMILAR_TO_PARTS).byUri(relation.getUri()));
    }

    // DESCRIBES -> Item
    public List<String> listItems(String id) {
        String uri = uriGenerator.from(Resource.Type.PART, id);
        return udm.find(Resource.Type.ITEM).from(Resource.Type.PART, uri);
    }

    public void removeItems(String id) {
        String uri = uriGenerator.from(Resource.Type.PART, id);
        udm.delete(Relation.Type.DESCRIBES).in(Resource.Type.PART, uri);
    }

    public RelationI getItems(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.PART, startId);
        String endUri = uriGenerator.from(Resource.Type.ITEM, endId);
        Optional<RelationI> result = udm.find(Relation.Type.DESCRIBES).btw(startUri, endUri).stream().map(relation -> new RelationI(relation.getUri(), relation.getCreationTime())).findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public void addItems(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.PART, startId);
        String endUri = uriGenerator.from(Resource.Type.ITEM, endId);
        udm.save(Relation.newDescribes(startUri, endUri));
    }

    public void removeItems(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.PART, startId);
        String iuri = uriGenerator.from(Resource.Type.ITEM, endId);
        udm.find(Relation.Type.DESCRIBES).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.DESCRIBES).byUri(relation.getUri()));
    }

    // DEALS_WITH -> Topic
    public List<String> listTopics(String id) {
        String uri = uriGenerator.from(Resource.Type.PART, id);
        return udm.find(Resource.Type.TOPIC).from(Resource.Type.PART, uri);
    }

    public void removeTopics(String id) {
        String uri = uriGenerator.from(Resource.Type.PART, id);
        udm.delete(Relation.Type.DEALS_WITH_FROM_PART).in(Resource.Type.PART, uri);
    }

    public DealsI getTopics(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.PART, startId);
        String endUri = uriGenerator.from(Resource.Type.TOPIC, endId);
        Optional<DealsI> result = udm.find(Relation.Type.DEALS_WITH_FROM_PART).btw(startUri, endUri).stream().map(relation -> new DealsI(relation.getUri(), relation.getCreationTime(), relation.getWeight())).findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public void addTopics(String startId, String endId, WeightDomainI weightI) {
        String startUri = uriGenerator.from(Resource.Type.PART, startId);
        String endUri = uriGenerator.from(Resource.Type.TOPIC, endId);
        DealsWithFromPart deals = Relation.newDealsWithFromPart(startUri, endUri);
        deals.setWeight(weightI.getWeight());
        udm.save(deals);
    }

    public void removeTopics(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.PART, startId);
        String iuri = uriGenerator.from(Resource.Type.TOPIC, endId);
        udm.find(Relation.Type.DEALS_WITH_FROM_PART).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.DEALS_WITH_FROM_PART).byUri(relation.getUri()));
    }

}
