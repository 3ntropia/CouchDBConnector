package org.connector.dao.query.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.connector.model.Document;

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