/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.api.services;

import org.librairy.api.dao.SessionManager;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.UDM;
import org.librairy.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 18/01/16.
 */

public abstract class AbstractResourceService<T extends Resource> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractResourceService.class);

    private final Resource.Type type;

    @Autowired
    protected UDM udm;

    @Autowired
    protected SessionManager sessionManager;

    @Autowired
    URIGenerator uriGenerator;

    public AbstractResourceService(Resource.Type type) {
        this.type = type;
    }


    public T create(T resource) throws Exception {
        LOG.info("Trying to create: " + resource);
        udm.save(resource);
        return resource;
    }

    public T update(String id, T resource) {
        String uri = uriGenerator.from(type, id);
        LOG.debug("updating by topic: " + uri);
        Optional<Resource> result = udm.read(type).byUri(uri);
        if (!result.isPresent()) {
            throw new RuntimeException("Resource does not exist with topic: " + uri);
        }
        T original = (T) result.get();
        BeanUtils.copyProperties(resource, original);
        udm.save(original);
        return original;
    }


    public void remove(String id) {
        String uri = uriGenerator.from(type, id);
        LOG.debug("removing by topic: " + uri);
        udm.delete(type).byUri(uri);
    }

    public void removeAll() {
        udm.delete(type).all();
    }

    public List<String> list() {
        return udm.find(type).all().stream().map(res -> res.getUri()).distinct().collect(Collectors.toList());
    }


    public T get(String id) {
        String uri = uriGenerator.from(type, id);
        LOG.debug("getting by topic: " + uri);
        Optional<Resource> result = udm.read(type).byUri(uri);
        switch (type){
            case DOCUMENT:

                break;
            default:
                break;
        }
        if (!result.isPresent())
            return null; //TODO Handle empty result
        return (T) result.get();
    }


}
