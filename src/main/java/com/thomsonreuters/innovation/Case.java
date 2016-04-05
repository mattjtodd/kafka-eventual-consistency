package com.thomsonreuters.innovation;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Document(indexName = "case", type = "case", shards = 1, replicas = 0, refreshInterval = "-1")
public class Case {

    @Id
    private String id;

    @Basic(optional = false)
    private String name;

    Case() {
        // for hibernate
    }

    public Case(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
