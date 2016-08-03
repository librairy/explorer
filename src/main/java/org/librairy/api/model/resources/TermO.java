package org.librairy.api.model.resources;

import lombok.Data;
import org.librairy.model.domain.resources.Term;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
@XmlRootElement(name = "term")
@XmlAccessorType(XmlAccessType.FIELD)
public class TermO extends Term{

    @XmlAttribute
    String uri;

    @XmlAttribute
    String content;
}
