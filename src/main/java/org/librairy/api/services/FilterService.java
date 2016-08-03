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
import org.librairy.modeler.lda.helper.SparkHelper;
import org.librairy.modeler.lda.models.similarity.RelationalSimilarity;
import org.librairy.storage.generator.URIGenerator;
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
public class FilterService extends AbstractResourceService<Filter> {

    @Autowired
    SparkService sparkService;

    @Value("${librairy.modeler.folder}")
    String modelFolder;

    @Value("${librairy.vocabulary.folder}")
    String vocabularyFolder;

    //TODO Update Camel to 2.17.x to define this as query param
    String domainId = "4f56ab24bb6d815a48b8968a3b157470";

    //TODO Update Camel to 2.17.x to define this as query param
    String n = "20";

    private static final Logger LOG = LoggerFactory.getLogger(FilterService.class);

    public FilterService() {
        super(Resource.Type.FILTER);
    }


    public List<WeightResourceI> listSimilarDocuments(String id) throws IOException, ClassNotFoundException {
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        String dUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);
        Integer top = Integer.valueOf(n);
        Filter filter = udm.read(Resource.Type.FILTER).byUri(uri).get().asFilter();
        return findSimilar(filter.getContent(), dUri, top);
    }


    public List<WeightResourceI> listSimilarItems(String id) throws IOException, ClassNotFoundException {
        //TODO Handle Items
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        String dUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);
        Integer top = Integer.valueOf(n);
        Filter filter = udm.read(Resource.Type.FILTER).byUri(uri).get().asFilter();
        return findSimilar(filter.getContent(), dUri, top);
    }


    public List<WeightResourceI> listSimilarParts(String id) throws IOException, ClassNotFoundException {
        //TODO Handle Parts
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        String dUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);
        Integer top = Integer.valueOf(n);
        Filter filter = udm.read(Resource.Type.FILTER).byUri(uri).get().asFilter();
        return findSimilar(filter.getContent(), dUri, top);
    }


    public List<WeightResourceI> listSimilar(String id) throws IOException, ClassNotFoundException {
        //TODO Handle All the types
        String uri = uriGenerator.from(Resource.Type.FILTER, id);
        String dUri = uriGenerator.from(Resource.Type.DOMAIN, domainId);
        Integer top = Integer.valueOf(n);
        Filter filter = udm.read(Resource.Type.FILTER).byUri(uri).get().asFilter();
        return findSimilar(filter.getContent(), dUri, top);
    }


    private List<WeightResourceI> findSimilar(String inputText, String domainUri, int top) throws IOException,
            ClassNotFoundException {



        OnlineLDABuilder ldaBuilder = new OnlineLDABuilder();
        ldaBuilder.setSparkHelper(sparkService.getSparkHelper());


        // Load Model and Vocabulary
        String domainId = URIGenerator.retrieveId(domainUri);
        Path modelPath = Paths.get(modelFolder,domainId);
        Path vocabularyPath = Paths.get(vocabularyFolder,domainId);

        LocalLDAModel model = LocalLDAModel.load(sparkService.getSparkHelper().getSc().sc(),modelPath.toString());
        CountVectorizerModel cvModel = CountVectorizerModel.load(vocabularyPath.toString());
        Tuple2<Object, Vector> tuple = new Tuple2<Object,Vector>(0l, Vectors.dense(new double[]{1.0}));


        // Parsing input text
        List<Row> inputRows = Arrays.asList(new Row[]{RowFactory.create("test-uri", inputText)});
        DataFrame inputDF = ldaBuilder.preprocess(inputRows);
        RDD<Tuple2<Object, Vector>> inputDocs = cvModel.transform(inputDF)
                .select("uri", "features")
                .map(new RowToPair(), ClassTag$.MODULE$.<Tuple2<Object, Vector>>apply(tuple.getClass()));
        RDD<Tuple2<Object, Vector>> inputTopics = model.topicDistributions(inputDocs);

        // Corpus Data Frame
        LOG.info("Reading item uris..");
        ConcurrentHashMap<Long,String> itemRegistry = new ConcurrentHashMap<>();
        List<Row> corpusRows = Collections.emptyList();
        Path corpusPath = Paths.get(modelFolder,domainId,"corpus.gz");

        if (corpusPath.toFile().exists()){
            corpusRows = (List<Row>) deserialize(corpusPath.toString());
        }else{
            List<String> itemsUri = udm.find(Resource.Type.ITEM).from(Resource.Type.DOMAIN, domainUri);
            corpusRows = itemsUri.parallelStream().
                map(uri -> udm.read(Resource.Type.ITEM).byUri(uri)).
                filter(res -> res.isPresent()).map(res -> (Item) res.get()).
                map(item -> RowFactory.create(item.getUri(), item.getTokens())).
                collect(Collectors.toList());
            serialize(corpusRows,corpusPath.toString());
        }

        corpusRows.parallelStream().forEach(row -> {
            String uri = String.valueOf(row.get(0));
            Long id = RowToPair.from(uri);
            itemRegistry.put(id,uri);
        });

        DataFrame corpusDF = ldaBuilder.preprocess(corpusRows);
        RDD<Tuple2<Object, Vector>> inputCorpus = cvModel.transform(corpusDF)
                .select("uri", "features")
                .map(new RowToPair(), ClassTag$.MODULE$.<Tuple2<Object, Vector>>apply(tuple.getClass()));
        RDD<Tuple2<Object, Vector>> corpusTopics = model.topicDistributions(inputCorpus);


        // Comparison
        JavaRDD<Tuple2<Object, Vector>> inputRDD = inputTopics.toJavaRDD().repartition(100).cache();
        inputRDD.take(1);
        JavaRDD<Tuple2<Object, Vector>> corpusRDD = corpusTopics.toJavaRDD();


        List<Tuple2<Tuple2<Object, Vector>, Tuple2<Object, Vector>>> itemsPair = inputRDD.cartesian(corpusRDD)
//                .filter(x -> x._1()._1.toString().compareTo(x._2()._1.toString()) > 0)
                .collect();

        LOG.info("Calculating similarities: " + itemsPair.size());
        ConcurrentHashMap<Long, String> finalItemRegistry1 = itemRegistry;
        List<Tuple2<Double, String>> similars = itemsPair.stream().map(pair -> {

            Vector v1 = pair._1._2;
            List<Relationship> v1List = new ArrayList<>();
            for (int i = 0; i < v1.size(); i++) {
                double[] array = v1.toArray();
                v1List.add(new Relationship(String.valueOf(i), array[i]));
            }

            Vector v2 = pair._2._2;
            List<Relationship> v2List = new ArrayList<>();
            for (int i = 0; i < v2.size(); i++) {
                double[] array = v2.toArray();
                v2List.add(new Relationship(String.valueOf(i), array[i]));
            }


            Double similarity = RelationalSimilarity.between(v1List, v2List);

            String itemUri = finalItemRegistry1.get(pair._2._1);

            return new Tuple2<Double, String>(similarity, itemUri);
        }).sorted(new Comparator<Tuple2<Double, String>>() {
            @Override
            public int compare(Tuple2<Double, String> o1, Tuple2<Double, String> o2) {
                return -o1._1.compareTo(o2._1);
            }
        }).limit(top).collect(Collectors.toList());


        List<WeightResourceI> simDocs = similars.stream().map(doc -> {

            List<String> docUri = udm.find(Resource.Type.DOCUMENT).from(Resource.Type.ITEM, doc._2);

            Document document = udm.read(Resource.Type.DOCUMENT).byUri(docUri.get(0)).get().asDocument();

            WeightResourceI weighResource = new WeightResourceI();
            weighResource.setWeight(doc._1);
            weighResource.setResource(document.getUri());
            weighResource.setDescription(document.getTitle());

            return weighResource;
        }).collect(Collectors.toList());

        LOG.info("Similar Resources: " + simDocs);

        return simDocs;
    }


    private void serialize(Object object, String path) throws IOException {
        FileOutputStream fout = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(object);
        out.close();
        fout.close();
        LOG.info("Object serialized to: " + path);
    }

    private Object deserialize(String path) throws IOException, ClassNotFoundException {
        FileInputStream fin = new FileInputStream(path);
        ObjectInputStream oin = new ObjectInputStream(fin);
        Object value = oin.readObject();
        oin.close();
        fin.close();
        return value;
    }


}
