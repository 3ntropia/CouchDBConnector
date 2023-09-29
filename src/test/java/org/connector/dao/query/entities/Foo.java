package org.connector.dao.query.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.connector.model.Document;

import static org.connector.dao.ConstantTest.INMUTABLE_STRING;

@Getter
@Setter
@JsonIgnoreProperties
public class Foo extends Document {
    public Foo(String id){
        this.setId(id);
    }

}
