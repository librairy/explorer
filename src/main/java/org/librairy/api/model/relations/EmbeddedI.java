package org.librairy.api.model.relations;

import lombok.Data;
import org.librairy.model.utils.TimeUtils;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class EmbeddedI {

    public EmbeddedI() {
    }

    ;

    public EmbeddedI(String uri, String creationTime) {
        this.uri = uri;
        this.creationTime = creationTime;
    }

    String uri;

    String creationTime = TimeUtils.asISO();
}
