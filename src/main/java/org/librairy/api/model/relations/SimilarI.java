/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.model.relations;

import lombok.Data;
import org.librairy.model.utils.TimeUtils;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class SimilarI {

    public SimilarI() {
    }

    ;

    public SimilarI(String uri, String creationTime, Double weight, String domain) {
        this.uri = uri;
        this.creationTime = creationTime;
        this.weight = weight;
        this.domain = domain;
    }

    String uri;

    String creationTime = TimeUtils.asISO();

    Double weight;

    String domain;
}
