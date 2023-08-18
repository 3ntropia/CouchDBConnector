package org.connector.dao.query.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.connector.model.Document;

import java.util.List;

import static org.connector.dao.ConstantTest.INMUTABLE_STRING;


@Getter
@Setter
@JsonIgnoreProperties
public class SomeClass extends Document {
    private String field;
    private String extraField;

    private final String INMUTABLE = INMUTABLE_STRING;
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
