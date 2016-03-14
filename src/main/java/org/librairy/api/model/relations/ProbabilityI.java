package org.librairy.api.model.relations;

import lombok.Data;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class ProbabilityI {

    Double cvalue;

    Long times;

    Double consensus;

    Double pertinence;

    Long subtermsOf;

    Long supertermsOf;

    Double termhood;

    Double probability;
}
