package com.thomsonreuters.innovation;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "case", type = "case", shards = 1, replicas = 0, refreshInterval = "-1")
public class ElasticCase {

    @org.springframework.data.annotation.Id
    private String eventId;

    private String name;

    ElasticCase() {

    }

    public ElasticCase(String eventId, String name) {
        this.eventId = eventId;
        this.name = name;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
