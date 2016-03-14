package org.librairy.api.converters;

import org.apache.camel.Converter;
import org.apache.camel.TypeConversionException;
import org.librairy.api.model.resources.DomainI;
import org.librairy.model.domain.resources.Domain;
import org.librairy.model.domain.resources.Resource;

/**
 * Created by cbadenes on 11/02/16.
 */
@Converter
public class ResourceConverter {

    @Converter
    public static Domain toResource(DomainI facade) throws TypeConversionException {
        Domain domain = Resource.newDomain();
        domain.setName(facade.getName());
        domain.setDescription(facade.getDescription());
        return domain;
    }
}
