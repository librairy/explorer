package org.librairy.api.converters;

import org.apache.camel.Converter;
import org.apache.camel.TypeConversionException;
import org.apache.commons.beanutils.BeanUtils;
import org.librairy.api.model.resources.DomainI;
import org.librairy.api.model.resources.FilterI;
import org.librairy.api.model.resources.SourceI;
import org.librairy.api.model.resources.TermO;
import org.librairy.model.domain.resources.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

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

    //TODO Document

    //TODO Item

    //TODO Part

    //TODO Topic

    //TODO Word

    //TODO Term
    @Converter
    public static TermO toResource(Term term) throws TypeConversionException, InvocationTargetException,
            IllegalAccessException {
        LOG.info("Trying to convert Term to TermO...");
        TermO tf = new TermO();
        BeanUtils.copyProperties(tf,term);
        return tf;
    }


}
