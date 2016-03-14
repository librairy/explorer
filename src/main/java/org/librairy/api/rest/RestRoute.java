package org.librairy.api.rest;

import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.apache.commons.lang.StringUtils;
import org.librairy.api.model.relations.WeightI;

/**
 * Created by cbadenes on 25/02/16.
 */
public abstract class RestRoute {


    private final String plural;
    private final String singular;

    public RestRoute(String plural, String singular) {
        this.plural = plural;
        this.singular = singular;
    }


    public abstract RestDefinition configure(RestsDefinition definitions);


    protected RestDefinition addResourceCRUD(RestsDefinition definitions, Class inType, Class outType) {
        return definitions.rest("/" + plural).description("rest service for " + singular + " management")

                .post().description("Add a new " + singular + "").type(inType).outType(outType)
                .produces("application/json").to("bean:" + singular + "Service?method=create")

                .get("/")
                .description("List all existing " + plural + "").outTypeList(String.class)
                .produces("application/json").to("bean:" + singular + "Service?method=list")

                .get("/{id}")
                .description("More details about  a " + singular + " by id").outType(outType)
//                .param().name("verbose").type(RestParamType.query).defaultValue("false").description("Verbose order details").endParam()
                .produces("application/json").to("bean:" + singular + "Service?method=get(${header.id})")

                .delete("/").description("Remove all existing " + plural + "")
                .produces("application/json").to("bean:" + singular + "Service?method=removeAll()")

                .delete("/{id}").description("Remove an existing " + singular + "")
                .produces("application/json").to("bean:" + singular + "Service?method=remove(${header.id})")

                .put("/{id}").description("Update an existing " + singular + "").type(inType).outType(outType)
                .produces("application/json").to("bean:" + singular + "Service?method=update");
    }


    protected RestDefinition addRelationCRUD(RestDefinition rest, String related, Class inType, Class outType, String relation) {

        RestDefinition definition = rest;

        //get(/id)
        definition = rest.get("/{id}/" + related).description("List all " + related + " " + relation + " a " + singular).outTypeList(String.class)
                .produces("application/json").to("bean:" + singular + "Service?method=list" + StringUtils.capitalize(related) + "(${header.id})");

        //delete(/id)
        definition = definition.delete("/{id}/" + related).description("Remove all existing " + related + " " + relation + " a " + singular)
                .produces("application/json").to("bean:" + singular + "Service?method=remove" + StringUtils.capitalize(related) + "(${header.id})");

        //get(/id/relations/id)
        definition = definition.get("/{id}/" + related + "/{rid}").description("Details about the relation between " + related + " and " + plural);
        if (outType != null) definition = definition.outTypeList(outType);
        definition = definition.produces("application/json").to("bean:" + singular + "Service?method=get" + StringUtils.capitalize(related) + "(${header.id},${header.rid})");

        //post(/id/relations/id)
        definition = definition.post("/{sid}/" + related + "/{did}").description("Add " + related + " to a " + singular);
        if (inType != null) definition = definition.type(WeightI.class);
        definition = definition.produces("application/json").to("bean:" + singular + "Service?method=add" + StringUtils.capitalize(related) + "(${header.sid},${header.did},${body})");


        //delete(/id/relations/id)
        definition = definition.delete("/{id}/" + related + "/{rid}").description("Remove the existing relation between " + related + " and " + plural)
                .produces("application/json").to("bean:" + singular + "Service?method=remove" + StringUtils.capitalize(related) + "(${header.id},${header.rid})");

        return definition;
    }


}
