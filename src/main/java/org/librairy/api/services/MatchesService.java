package org.librairy.api.services;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.mllib.clustering.LocalLDAModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.librairy.api.model.relations.WeightResourceI;
import org.librairy.model.domain.relations.Relationship;
import org.librairy.model.domain.resources.Document;
import org.librairy.model.domain.resources.Filter;
import org.librairy.model.domain.resources.Item;
import org.librairy.model.domain.resources.Resource;
import org.librairy.modeler.lda.builder.OnlineLDABuilder;
import org.librairy.modeler.lda.functions.RowToPair;
import org.librairy.modeler.lda.models.similarity.RelationalSimilarity;
import org.librairy.storage.generator.URIGenerator;
import org.librairy.storage.system.document.domain.DocumentDocument;
import org.librairy.storage.system.document.repository.DocumentDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import scala.Tuple2;
import scala.reflect.ClassTag$;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class MatchesService extends AbstractResourceService<Filter> {

    //TODO Update Camel to 2.17.x to define this as query param
    String n = "20";

    @Autowired
    DocumentDocumentRepository documentRepository;

    private static final Logger LOG = LoggerFactory.getLogger(MatchesService.class);

    public MatchesService() {
        super(Resource.Type.FILTER);
    }


    public List<WeightResourceI> listMatchedDocuments(String id) throws IOException, ClassNotFoundException {
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        Integer top = Integer.valueOf(n);
        return null;
    }


    public List<WeightResourceI> listMatchedItems(String id) throws IOException, ClassNotFoundException {
        //TODO Handle Items
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        Integer top = Integer.valueOf(n);
        return null;
    }


    public List<WeightResourceI> listMatchedParts(String id) throws IOException, ClassNotFoundException {
        //TODO Handle Parts
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        Integer top = Integer.valueOf(n);
        return null;
    }


    public List<WeightResourceI> listMatches(String id) throws IOException, ClassNotFoundException {
        //TODO Handle All the types
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        Integer top = Integer.valueOf(n);
        return null;
    }
}
