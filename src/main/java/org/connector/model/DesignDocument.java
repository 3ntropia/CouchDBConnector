package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Reference
 * https://docs.couchdb.org/en/latest/ddocs/ddocs.html#creation-and-structure
 *
 * Body Example:
 * {
 *     "_id": "_design/example",
 *     "views": {
 *         "view-number-one": {
 *             "map": "function (doc) {
 *                          if (doc.type === 'post' && doc.tags && Array.isArray(doc.tags)) {
 *                              doc.tags.forEach(function (tag) {
 *                                  emit(tag.toLowerCase(), 1);
 *                              });
 *                          }
 *                      }"
 *         },
 *         "view-number-two":{
 *             "map":"function (doc) { }",
 *             "reduce":"function (keys, values, rereduce) { }"
 *         }
 *      },
 *      "updates":{
 *          "updatefun1":"function(doc,req) { }",
 *          "updatefun2":"function(doc,req) { }"
 *      },
 *      "filters":{
 *          "filterfunction1":"function(doc, req){   }"
 *      },
 *      "validate_doc_update":"function(newDoc, oldDoc, userCtx, secObj) {   }",
 *      "language":"javascript"
 *      }
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record DesignDocument(
        @JsonProperty("_id")
        String id,
        Map<String,Map<String,String>> views,
        Map<String,String> updates,
        Map<String,String> filters,
        @JsonProperty("validate_doc_update")
        String validateDocUpdate,
        String language) {

}
