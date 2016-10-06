/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.converters;

import org.apache.camel.Converter;
import org.apache.camel.TypeConversionException;
import org.librairy.api.model.resources.DomainI;
import org.librairy.api.model.resources.FilterI;
import org.librairy.api.model.resources.PathI;
import org.librairy.api.model.resources.SourceI;
import org.librairy.model.domain.resources.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 11/02/16.
 */
@Converter
public class ResourceConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceConverter.class);

    @Converter
    public static Domain toResource(DomainI facade) throws TypeConversionException {
        Domain resource = Resource.newDomain(facade.getName());
        resource.setDescription(facade.getDescription());
        return resource;
    }

    @Converter
    public static Source toResource(SourceI facade) throws TypeConversionException {
        Source resource = Resource.newSource(facade.getName());
        resource.setDescription(facade.getDescription());
        resource.setUrl(facade.getUrl());
        return resource;
    }

    @Converter
    public static Filter toResource(FilterI facade) throws TypeConversionException {
        return Resource.newFilter(facade.getContent());
    }

    @Converter
    public static Path toResource(PathI facade) throws TypeConversionException {
        return Resource.newPath(facade.getStart(),facade.getEnd());
    }

    //TODO Document

    //TODO Item

    //TODO Part

    //TODO Topic

    //TODO Word

    //TODO Term
}
