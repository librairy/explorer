package org.librairy.api.model.relations;

import lombok.Data;
import org.librairy.model.utils.TimeUtils;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class AppearedI {


    public AppearedI() {
    }

    ;

    public AppearedI(String uri, String creationTime, Double cvalue, Long times, Double consensus, Double pertinence, Long subtermsOf, Long supertermsOf, Double termhood, Double probability) {
        this.uri = uri;
        this.creationTime = creationTime;
        this.cvalue = cvalue;
        this.times = times;
        this.consensus = consensus;
        this.pertinence = pertinence;
        this.subtermsOf = subtermsOf;
        this.supertermsOf = supertermsOf;
        this.termhood = termhood;
        this.probability = probability;
    }

    String uri;

    String creationTime = TimeUtils.asISO();

    Double cvalue;

    Long times;

    Double consensus;

    Double pertinence;

    Long subtermsOf;

    Long supertermsOf;

    Double termhood;

    Double probability;

}
