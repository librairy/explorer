/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class RestRouteBuilder extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RestRouteBuilder.class);

    @Value("${librairy.api.rest.port}")
    protected Integer port;

    @Autowired
    List<RestRoute> routes;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .scheme("http")
                .bindingMode(RestBindingMode.auto)
                .skipBindingOnErrorCode(true)
                .jsonDataFormat("json-jackson")
                .xmlDataFormat("jaxb")
                .enableCORS(false)
                .componentProperty("minThreads", "1")
                .componentProperty("maxThreads", "10")
                .componentProperty("maxConnectionsPerHost", "-1")
                .componentProperty("maxTotalConnections", "-1")
                .dataFormatProperty("include", "NON_NULL")
                .dataFormatProperty("prettyPrint", "true")
                .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES,ADJUST_DATES_TO_CONTEXT_TIME_ZONE")
//                .dataFormatProperty("json.out.disableFeatures", "WRITE_NULL_MAP_VALUES")
                .dataFormatProperty("xml.out.mustBeJAXBElement", "false")
                .contextPath("api/0.2")
                .port(port);


        // Configure REST routes
        this.getRestCollection().setCamelContext(this.getContext());
        routes.forEach(route -> configureRest(route.configure(getRestCollection())));

    }

}
