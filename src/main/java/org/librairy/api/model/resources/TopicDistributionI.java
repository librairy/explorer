/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.model.resources;

import lombok.Data;

import java.util.List;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class TopicDistributionI {

    String topic;

    Double relevance;

    List<WordDistributionI> words;

    public void setWords(List<WordDistributionI> words){
        this.words = words;
        this.words.sort((o1, o2) -> -o1.getRelevance().compareTo(o2.getRelevance()));
    }
}
