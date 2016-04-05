package com.thomsonreuters.innovation;

import javax.persistence.*;

@Entity
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
