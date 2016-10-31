/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.model.resources;

import lombok.Data;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class WordDistributionI {

    String word;

    Double relevance;

    public WordDistributionI(String word, Double relevance){
        this.word = word;
        this.relevance = relevance;
    }
}
