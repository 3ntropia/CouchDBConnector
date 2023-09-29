package org.connector.dao.query.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.connector.model.Document;

@Getter
@Setter
@JsonIgnoreProperties
@NoArgsConstructor
public class LightSomeClass extends Document {

    private String field;
    public LightSomeClass(String s) {
        this.setId(s);
    }

}
