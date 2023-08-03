package org.connector.query.operators.array;

import org.connector.query.CollectionCondition;

/**
 * The $ElementMatchCollection operator matches and returns all documents that contain 
 * a collection of fields (more than one) with all the elements matching the supplied query criteria. 
 * Below is an example used with the primary index (_all_docs)
{
	"selector": {
		"document.tenantId":"TENANT_NAME_01",
		"document.sandbox":"SANDOX_01",
		"document.status": "OPENED",
		"document.tickets": {
			"$elemMatch": {
				"status": "OPENED",
				"tableNumber": "A1"
			}
		}
	}
}
 */
public class ElementMatchCollection extends CollectionCondition {
    private String field;
    
	public ElementMatchCollection(String field) {
        setField(field);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
    
    public String toString() {
        return "{\"" + getField() + "\": {\"$elemMatch\":{\"$and\" : " + getConditions().toString() + "}}}";
    }
}
