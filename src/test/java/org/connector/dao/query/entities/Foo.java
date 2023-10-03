package org.connector.dao.query.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.connector.model.Document;

import static org.connector.dao.ConstantTest.INMUTABLE_STRING;

@Getter
@Setter
@JsonIgnoreProperties
@NoArgsConstructor
public class Foo extends Document {
    public Foo(String id){
        this.setId(id);
    }

}
