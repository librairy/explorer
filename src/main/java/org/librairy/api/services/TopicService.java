package org.librairy.api.services;

import org.librairy.api.model.relations.AnalysisI;
import org.librairy.api.model.relations.EmergesI;
import org.librairy.api.model.relations.MentionsI;
import org.librairy.api.model.relations.WeightTimesI;
import org.librairy.model.domain.relations.EmergesIn;
import org.librairy.model.domain.relations.Mentions;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.domain.resources.Topic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class TopicService extends AbstractResourceService<Topic> {

    public TopicService() {
        super(Resource.Type.TOPIC);
    }

    // MENTIONS -> Word
    public List<String> listWords(String id) {
        String uri = uriGenerator.from(Resource.Type.TOPIC, id);
        return udm.find(Resource.Type.WORD).from(Resource.Type.TOPIC, uri);
    }

    public void removeWords(String id) {
        String uri = uriGenerator.from(Resource.Type.TOPIC, id);
        udm.delete(Relation.Type.MENTIONS_FROM_TOPIC).in(Resource.Type.TOPIC, uri);
    }

    public MentionsI getWords(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.TOPIC, startId);
        String endUri = uriGenerator.from(Resource.Type.WORD, endId);
        Optional<MentionsI> result = udm.find(Relation.Type.MENTIONS_FROM_TOPIC).btw(startUri, endUri).stream().map(relation -> (Mentions) relation).map(relation -> new MentionsI(relation.getUri(), relation.getCreationTime(), relation.getTimes(), relation.getWeight())).findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public void addWords(String startId, String endId, WeightTimesI rel) {
        String startUri = uriGenerator.from(Resource.Type.TOPIC, startId);
        String endUri = uriGenerator.from(Resource.Type.WORD, endId);
        Mentions relation = Relation.newMentionsFromTopic(startUri, endUri);
        relation.setTimes(rel.getTimes());
        relation.setWeight(rel.getWeight());
        udm.save(relation);
    }

    public void removeWords(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.TOPIC, startId);
        String iuri = uriGenerator.from(Resource.Type.WORD, endId);
        udm.find(Relation.Type.MENTIONS_FROM_TOPIC).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.MENTIONS_FROM_TOPIC).byUri(relation.getUri()));
    }

    // EMERGES_IN -> Domain
    public List<String> listDomains(String id) {
        String uri = uriGenerator.from(Resource.Type.TOPIC, id);
        return udm.find(Resource.Type.DOMAIN).from(Resource.Type.TOPIC, uri);
    }

    public void removeDomains(String id) {
        String uri = uriGenerator.from(Resource.Type.TOPIC, id);
        udm.delete(Relation.Type.EMERGES_IN).in(Resource.Type.TOPIC, uri);
    }

    public EmergesI getDomains(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.TOPIC, startId);
        String endUri = uriGenerator.from(Resource.Type.DOMAIN, endId);
        Optional<EmergesI> result = udm.find(Relation.Type.EMERGES_IN).btw(startUri, endUri).stream().map(relation -> (EmergesIn) relation).map(relation -> new EmergesI(relation.getUri(), relation.getCreationTime(), relation.getAnalysis())).findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public void addDomains(String startId, String endId, AnalysisI rel) {
        String startUri = uriGenerator.from(Resource.Type.TOPIC, startId);
        String endUri = uriGenerator.from(Resource.Type.DOMAIN, endId);
        EmergesIn relation = Relation.newEmergesIn(startUri, endUri);
        relation.setAnalysis(rel.getAnalysis());
        udm.save(relation);
    }

    public void removeDomains(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.TOPIC, startId);
        String iuri = uriGenerator.from(Resource.Type.DOMAIN, endId);
        udm.find(Relation.Type.EMERGES_IN).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.EMERGES_IN).byUri(relation.getUri()));
    }
}
