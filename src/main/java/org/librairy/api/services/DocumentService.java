/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.services;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.google.common.base.Strings;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.librairy.api.model.relations.*;
import org.librairy.api.model.resources.DocumentI;
import org.librairy.api.model.resources.SimilarityI;
import org.librairy.model.domain.relations.DealsWithFromDocument;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.relations.SimilarToDocuments;
import org.librairy.model.domain.resources.Document;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.utils.TimeUtils;
import org.librairy.storage.system.column.repository.UnifiedColumnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class DocumentService extends AbstractResourceService<Document> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentService.class);

    @Autowired
    UnifiedColumnRepository columnRepository;

    public DocumentService() {
        super(Resource.Type.DOCUMENT);
    }

    @Override
    public Document get(String id) {
        Document document = super.get(id);
        LOG.info("Getting in Document Service: " + document);
        // TODO Pending to be included by a query parameter
        if (document != null) {
            document.setContent(null);
            document.setTokens(null);
            document.setUri("http://drinventor.eu/SingleDocVis.php?id="+id);
        }
        return document;
    }

    public Document create(DocumentI resource) throws Exception {
        LOG.info("Trying to create: " + resource);
        Document document = Resource.newDocument(resource.getTitle());
        BeanUtils.copyProperties(document, resource);

        if (Strings.isNullOrEmpty(document.getFormat())) document.setFormat("json");
        if (Strings.isNullOrEmpty(document.getRetrievedFrom())) document.setRetrievedFrom("api");
        if (Strings.isNullOrEmpty(document.getRetrievedOn())) document.setRetrievedOn(TimeUtils.asISO());
        udm.save(document);
        LOG.info("New document created: " + document);
        return document;
    }

    // BUNDLES -> Item
    public List<String> listItems(String id) {
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        return udm.find(Resource.Type.ITEM).from(Resource.Type.DOCUMENT, uri).stream().map(res->res.getUri()).collect(Collectors.toList());
    }

    public void removeItems(String id) {
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);

        udm.find(Relation.Type.BUNDLES).from(Resource.Type.DOCUMENT, uri).forEach(rel->udm.delete(Relation.Type
                .BUNDLES).byUri(rel.getUri()));
    }

    public RelationI getItems(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri = uriGenerator.from(Resource.Type.ITEM, endId);
        Optional<RelationI> result = udm.find(Relation.Type.BUNDLES).btw(startUri, endUri).stream().map(relation -> new RelationI(relation.getUri(), relation.getCreationTime())).findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public void addItems(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri = uriGenerator.from(Resource.Type.ITEM, endId);
        udm.save(Relation.newBundles(startUri, endUri));
    }

    public void removeItems(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String iuri = uriGenerator.from(Resource.Type.ITEM, endId);
        udm.find(Relation.Type.BUNDLES).btw(duri, iuri).forEach(relation -> udm.delete(Relation.Type.BUNDLES).byUri
                (relation.getUri()));
    }


    // SIMILAR_TO -> Documents
    public List<String> listDocuments(String id) {
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        return udm.find(Resource.Type.DOCUMENT).from(Resource.Type.DOCUMENT, uri).stream().map(res->res.getUri()).collect(Collectors.toList());
    }

    public void removeDocuments(String id) {
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).from(Resource.Type.DOCUMENT, uri).forEach(rel->udm.delete
                (Relation.Type.SIMILAR_TO_DOCUMENTS).byUri(rel.getUri()));
    }

    public SimilarI getDocuments(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        Optional<SimilarI> result = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).btw(startUri, endUri).stream().map(relation -> new SimilarI(relation.getUri(), relation.getCreationTime(), relation.getWeight(), ((SimilarToDocuments) relation).getDomain())).findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public void addDocuments(String startId, String endId, WeightDomainI rel) {
        String startUri = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        SimilarToDocuments relation = Relation.newSimilarToDocuments(startUri, endUri,rel.getDomain());
        relation.setDomain(rel.getDomain());
        relation.setWeight(rel.getWeight());
        udm.save(relation);
    }

    public void removeDocuments(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String iuri = uriGenerator.from(Resource.Type.DOCUMENT, endId);
        udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).btw(duri, iuri).forEach(relation -> udm.delete(Relation.Type
                .SIMILAR_TO_DOCUMENTS).byUri(relation.getUri()));
    }

    // DEALS_WITH -> Topic
    public List<String> listTopics(String id) {
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        return udm.find(Resource.Type.TOPIC).from(Resource.Type.DOCUMENT, uri).stream().map(res->res.getUri()).collect(Collectors.toList());
    }

    public void removeTopics(String id) {
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        udm.find(Relation.Type.DEALS_WITH_FROM_DOCUMENT).from(Resource.Type.DOCUMENT, uri).forEach(rel->udm.delete
                (Relation.Type.DEALS_WITH_FROM_DOCUMENT).byUri(rel.getUri()));
    }

    public DealsI getTopics(String startId, String endId) {
        String startUri = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri = uriGenerator.from(Resource.Type.TOPIC, endId);
        Optional<DealsI> result = udm.find(Relation.Type.DEALS_WITH_FROM_DOCUMENT).btw(startUri, endUri).stream().map(relation -> new DealsI(relation.getUri(), relation.getCreationTime(), relation.getWeight())).findFirst();
        return (result.isPresent()) ? result.get() : new DealsI();
    }

    public void addTopics(String startId, String endId, WeightI weightI) {
        String startUri = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String endUri = uriGenerator.from(Resource.Type.TOPIC, endId);
        DealsWithFromDocument relation = Relation.newDealsWithFromDocument(startUri, endUri);
        relation.setWeight(weightI.getWeight());
        udm.save(relation);
    }

    public void removeTopics(String startId, String endId) {
        String duri = uriGenerator.from(Resource.Type.DOCUMENT, startId);
        String iuri = uriGenerator.from(Resource.Type.TOPIC, endId);
        udm.find(Relation.Type.DEALS_WITH_FROM_DOCUMENT).btw(duri, iuri).forEach(relation -> udm.delete(Relation.Type
                .DEALS_WITH_FROM_DOCUMENT).byUri(relation.getUri()));
    }


    // SIMILAR_TO -> Documents
    public List<SimilarityI> listSimilarities(String id, String domainId) {
        String uri = uriGenerator.from(Resource.Type.DOCUMENT, id);
        String domainUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);


        ResultSet resultEnd = sessionManager.getSession().execute("select enduri, weight " +
                "from research.similarto where " +
                "starturi='" + uri + "';");


        ResultSet resultStart = sessionManager.getSession().execute("select starturi, weight " +
                "from research.similarto where " +
                "enduri='" + uri + "';");

        List<Row> rows = resultEnd.all();
        rows.addAll(resultStart.all());

        return rows
                .stream()
                .map(row -> new SimilarityI(row.getString(0),row.getDouble(1)))
                .sorted((a,b) -> -a.getScore().compareTo(b.getScore()))
                .collect(Collectors.toList());


    }

}
