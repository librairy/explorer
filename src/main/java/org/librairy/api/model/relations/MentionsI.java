package org.librairy.api.model.relations;

import lombok.Data;
import org.librairy.model.utils.TimeUtils;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class MentionsI {


    public MentionsI() {
    }

    ;

    public MentionsI(String uri, String creationTime, Long times, Double weight) {
        this.uri = uri;
        this.creationTime = creationTime;
        this.times = times;
        this.weight = weight;
    }

    String uri;

    String creationTime = TimeUtils.asISO();

    Long times;

    Double weight;


}
