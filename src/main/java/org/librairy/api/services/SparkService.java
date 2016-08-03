package org.librairy.api.services;

import lombok.Getter;
import org.librairy.modeler.lda.helper.SparkHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created on 12/07/16:
 *
 * @author cbadenes
 */
@Component
public class SparkService {

    @Getter
    private SparkHelper sparkHelper;

    @PostConstruct
    public void setup(){
        this.sparkHelper = new SparkHelper();
        sparkHelper.setMemory(String.valueOf(Runtime.getRuntime().maxMemory()/8));
        sparkHelper.setThreads(String.valueOf(Runtime.getRuntime().availableProcessors()/2));
        sparkHelper.setup();
    }
}
