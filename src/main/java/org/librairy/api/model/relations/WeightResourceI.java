package org.librairy.api.model.relations;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
@XmlRootElement(name = "document")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeightResourceI {

    Double weight;

    String resource;

    String description;

    public WeightResourceI setWeight(Double weight){
        this.weight = weight;
        return this;
    }

    public WeightResourceI setResource(String resource){
        this.resource = resource;
        return this;
    }

    public WeightResourceI setDescription(String description){
        this.description = description;
        return this;
    }
}
