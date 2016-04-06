package com.thomsonreuters.innovation;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class JpaCase {
    @Id
    private String eventId;

    private String name;

    JpaCase() {
        // for hibernate
    }

    public JpaCase(String eventId, String name) {
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
