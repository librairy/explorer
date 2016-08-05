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
        Domain resource = Resource.newDomain();
        resource.setName(facade.getName());
        resource.setDescription(facade.getDescription());
        return resource;
    }

    @Converter
    public static Source toResource(SourceI facade) throws TypeConversionException {
        Source resource = Resource.newSource();
        resource.setName(facade.getName());
        resource.setDescription(facade.getDescription());
        resource.setUrl(facade.getUrl());
        return resource;
    }

    @Converter
    public static Filter toResource(FilterI facade) throws TypeConversionException {
        Filter resource = Resource.newFilter();
        resource.setContent(facade.getContent());
        return resource;
    }

    @Converter
    public static Path toResource(PathI facade) throws TypeConversionException {
        Path resource = Resource.newPath();
        resource.setStart(facade.getStart());
        resource.setEnd(facade.getEnd());
        return resource;
    }

    //TODO Document

    //TODO Item

    //TODO Part

    //TODO Topic

    //TODO Word

    //TODO Term
}
