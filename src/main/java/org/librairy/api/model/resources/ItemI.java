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
public class ItemI {

    String authoredOn;

    String authoredBy;

    String contributedBy;

    String title;

    String subject;

    String format;

    String language;

    String description;

    String content;

    String tokens;
}
