package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetDatabaseInfoResponse(
        boolean compactRunning,
        String dbName,
        int diskFormatVersion,
        long docCount,
        long docDelCount,
        String instanceStartTime,
        Map<String,Object> props,
        long purgeSeq,
        String updateSeq,
        Cluster cluster,
        Sizes sizes) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Cluster(
            int n,
            int q,
            int r,
            int w) {

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Sizes(
            long active,
            long external,
            long file) {

    }
}
