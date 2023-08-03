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
@Data
@NoArgsConstructor
public class GetDatabaseInfoResponse {
    private boolean compactRunning;
    private String dbName;
    private int diskFormatVersion;
    private long docCount;
    private long docDelCount;
    private String instanceStartTime;
    private Map<String,Object> props;
    private long purgeSeq;
    private String updateSeq;
    private Cluster cluster;
    private Sizes sizes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    private static class Cluster {
        private int n;
        private int q;
        private int r;
        private int w;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    private static class Sizes {
        private long active;
        private long external;
        private long file;
    }
}
