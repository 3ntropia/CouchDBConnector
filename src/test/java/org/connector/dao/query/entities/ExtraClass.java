package org.connector.dao.query.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.connector.model.Document;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties
public class ExtraClass extends Document {
    private String field;
    private String id;

    public ExtraClass(){}

    public ExtraClass(String field, String id) {
        this.field = field;
        this.id = id;
    }

}