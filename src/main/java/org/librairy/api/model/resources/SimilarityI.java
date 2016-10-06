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
public class SimilarityI {

    String uri;

    Double score;

    public SimilarityI(String uri, Double score){
        this.uri = uri;
        this.score = score;
    }

}
