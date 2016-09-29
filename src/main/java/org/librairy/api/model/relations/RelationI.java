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
public class RelationI {

    public RelationI() {
    }

    ;

    public RelationI(String uri, String creationTime) {
        this.uri = uri;
        this.creationTime = creationTime;
    }

    String uri;

    String creationTime = TimeUtils.asISO();
}
