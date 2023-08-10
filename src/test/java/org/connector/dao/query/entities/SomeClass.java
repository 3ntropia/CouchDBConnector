package org.connector.dao.query.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.connector.model.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties
public class SomeClass extends Document {
    private String field;
    private String extraField;
    private List<InnerClass> innerClass;

    public SomeClass() {
    }

    public SomeClass(String field, String extraField, String id) {
        this.extraField = extraField;
        this.field = field;
        setId(id);
    }

    public SomeClass(String field, String extraField, String id, List<InnerClass> innerClass) {
        setId(id);
        this.field = field;
        this.extraField = extraField;
        this.innerClass = innerClass;
    }
}
