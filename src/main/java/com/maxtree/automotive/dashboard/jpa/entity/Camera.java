package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Camera {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Boolean inUse;

    public Camera() {}

    /**
     * Constructor
     *
     * @param name
     * @param description
     * @param inUse
     */
    public Camera(String name, String description, Boolean inUse) {
        this.name = name;
        this.description = description;
        this.inUse = inUse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    @Override
    public String toString() {
        return String.format(
                "Camera[id=%d, name='%s', description='%s', inUse='%s']",
                id, name, description, inUse);
    }
}
