/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.model.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public class TopicDistributionTest {

    private static final Logger LOG = LoggerFactory.getLogger(TopicDistributionTest.class);

    ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    public void complete() throws JsonProcessingException {

        Random random = new Random();
        int numTopics = 2;
        int numWordsPerTopic = 5;

        List<TopicDistributionI> topics = new ArrayList<>();

        for (int i = 0; i < numTopics ; i++){
            TopicDistributionI td = new TopicDistributionI();
            td.setTopic("http://drinventor.dia.fi.upm.es/topics/" + String.valueOf(i).hashCode());
            td.setRelevance(Double.valueOf(random.nextInt(10)/10));

            List<WordDistributionI> words = new ArrayList<>();

            for (int j = 0; j < numWordsPerTopic; j ++){
                words.add(new WordDistributionI("word" +j, Double.valueOf(random.nextInt(10)/10)));
            }

            td.setWords(words);

            topics.add(td);
        }

        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = jsonMapper.writeValueAsString(topics);
        LOG.info(json);

    }
}
