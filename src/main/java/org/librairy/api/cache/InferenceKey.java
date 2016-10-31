/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.cache;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Data
@EqualsAndHashCode
public class InferenceKey {

    private String filterUri;
    private String domainUri;

    public InferenceKey(String filterUri, String domainUri){
        this.filterUri  = filterUri;
        this.domainUri  = domainUri;
    }
}
