/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.librairy.annotation.EventListener;
import org.librairy.annotation.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.List;

/**
 * Created by cbadenes on 18/01/16.
 */
@Configuration("explorer")
@ComponentScan(basePackages = {
        "org.librairy.storage",
        "org.librairy.eventbus",
        "org.librairy.computing.storage",
        "org.librairy.computing.cluster",
        "org.librairy.computing.helper",
        "org.librairy.modeler.lda.helper",
        "org.librairy.modeler.lda.builder",
        "org.librairy.modeler.lda.optimizers",
        "org.librairy.api"
},
        excludeFilters = {@ComponentScan.Filter(EventListener.class),@ComponentScan.Filter(Publisher.class)})
@PropertySource({"classpath:api.properties", "classpath:boot.properties", "classpath:computing.properties", "classpath:lda-modeler.properties"})
public class Config {


    @Autowired
    List<RouteBuilder> builders;

    @Bean
    public SpringCamelContext camelContext(ApplicationContext applicationContext) throws Exception {
        SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
        for (RouteBuilder builder : builders) {
            camelContext.addRoutes(builder);
        }
        return camelContext;
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
