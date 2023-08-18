package org.connector.dao.query.entities;

import lombok.Getter;
import lombok.Setter;
import org.connector.model.Document;

@Getter
@Setter
public class InnerClass extends Document {
    private String property;

    public InnerClass(){}

    public InnerClass(String property) {
        this.property = property;
    }

    public InnerClass(String property, String id) {
        setId(id);
        this.property = property;
    }
}
