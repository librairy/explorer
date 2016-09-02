package org.librairy.api.services;

import org.librairy.api.model.relations.AppearedI;
import org.librairy.api.model.relations.MentionsI;
import org.librairy.api.model.relations.ProbabilityI;
import org.librairy.api.model.relations.WeightTimesI;
import org.librairy.model.domain.relations.AppearedIn;
import org.librairy.model.domain.relations.Mentions;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.domain.resources.Term;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class TermService extends AbstractResourceService<Term> {

    public TermService() {
        super(Resource.Type.TERM);
    }


    // APPEARED_IN -> Domain
    public List<String> listDomains(String id) {
        String uri = uriGenerator.from(Resource.Type.TERM, id);
        return udm.find(Resource.Type.DOMAIN).from(Resource.Type.TERM, uri).stream().map(res->res.getUri()).collect(Collectors.toList());
    }

    public void removeDomains(String id) {
        String uri = uriGenerator.from(Resource.Type.TERM, id);

        udm.find(Relation.Type.APPEARED_IN)
                .from(Resource.Type.TERM, uri).forEach(rel -> udm.delete(Relation.Type.APPEARED_IN).byUri(rel.getUri()));
    }

    public AppearedI getDomains(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.TERM, startId);
        String endUri = uriGenerator.from(Resource.Type.DOMAIN, endId);
        Optional<AppearedI> result = udm.find(Relation.Type.APPEARED_IN).btw(startUri, endUri).stream().map(relation -> (AppearedIn) relation).map(relation -> new AppearedI(relation.getUri(), relation.getCreationTime(), relation.getCvalue(), relation.getTimes(), relation.getConsensus(), relation.getPertinence(), relation.getSubtermOf(), relation.getSupertermOf(), relation.getTermhood(), relation.getProbability())).findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public void addDomains(String startId, String endId, ProbabilityI rel) {
        String startUri = uriGenerator.from(Resource.Type.TERM, startId);
        String endUri = uriGenerator.from(Resource.Type.DOMAIN, endId);
        AppearedIn relation = Relation.newAppearedIn(startUri, endUri);
        relation.setConsensus(rel.getConsensus());
        relation.setCvalue(rel.getCvalue());
        relation.setPertinence(rel.getPertinence());
        relation.setProbability(rel.getProbability());
        relation.setSubtermOf(rel.getSubtermsOf());
        relation.setSupertermOf(rel.getSupertermsOf());
        relation.setTermhood(rel.getTermhood());
        relation.setTimes(rel.getTimes());
        udm.save(relation);
    }

    public void removeDomains(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.TERM, startId);
        String iuri = uriGenerator.from(Resource.Type.DOMAIN, endId);
        udm.find(Relation.Type.APPEARED_IN).btw(duri, iuri).forEach(relation -> udm.delete(Relation.Type.APPEARED_IN)
                .byUri(relation.getUri()));
    }

    // MENTIONS -> Word
    public List<String> listWords(String id) {
        String uri = uriGenerator.from(Resource.Type.TERM, id);
        return udm.find(Resource.Type.WORD).from(Resource.Type.TERM, uri).stream().map(res->res.getUri()).collect(Collectors.toList());
    }

    public void removeWords(String id) {
        String uri = uriGenerator.from(Resource.Type.TERM, id);
        udm.find(Relation.Type.MENTIONS_FROM_TERM)
                .from(Resource.Type.TERM, uri).forEach(rel -> udm.delete(Relation.Type.MENTIONS_FROM_TERM).byUri(rel.getUri()));
    }

    public MentionsI getWords(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.TERM, startId);
        String endUri = uriGenerator.from(Resource.Type.WORD, endId);
        Optional<MentionsI> result = udm.find(Relation.Type.MENTIONS_FROM_TERM).btw(startUri, endUri).stream().map(relation -> (Mentions) relation).map(relation -> new MentionsI(relation.getUri(), relation.getCreationTime(), relation.getTimes(), relation.getWeight())).findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public void addWords(String startId, String endId, WeightTimesI rel) {
        String startUri = uriGenerator.from(Resource.Type.TERM, startId);
        String endUri = uriGenerator.from(Resource.Type.WORD, endId);
        Mentions relation = Relation.newMentionsFromTerm(startUri, endUri);
        relation.setTimes(rel.getTimes());
        relation.setWeight(rel.getWeight());
        udm.save(relation);
    }

    public void removeWords(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.TERM, startId);
        String iuri = uriGenerator.from(Resource.Type.WORD, endId);
        udm.find(Relation.Type.MENTIONS_FROM_TERM).btw(duri, iuri).forEach(relation -> udm.delete(Relation.Type
                .MENTIONS_FROM_TERM).byUri(relation.getUri()));
    }

}
