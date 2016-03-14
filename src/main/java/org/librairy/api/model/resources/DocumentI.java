package org.librairy.api.model.resources;

import lombok.Data;

/**
 * Created by cbadenes on 22/01/16.
 */
@Data
public class DocumentI {

    String title;

    String subject;

    String publishedOn;

    String publishedBy;

    String authoredOn;

    String authoredBy;

    String retrievedOn;

    String contributedBy;

    String format;

    String language;

    String description;

    String rights;

    String content;

    String tokens;
}
