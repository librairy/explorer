package org.librairy.api.model.relations;

import lombok.Data;
import org.librairy.model.utils.TimeUtils;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class EmergesI {


    public EmergesI() {
    }

    ;

    public EmergesI(String uri, String creationTime, String analysis) {
        this.uri = uri;
        this.creationTime = creationTime;
        this.analysis = analysis;
    }

    String uri;

    String creationTime = TimeUtils.asISO();

    String analysis;
}
