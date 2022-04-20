package com.mp.admin.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang3.Validate;

@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_id")
    private List<Content> contents;

    Resource() {
    }

    public Resource(String theTitle, String theDescription, List<Content> theContents) {
        Validate.notBlank(theTitle, "The title cannot be blank.");
        Validate.notBlank(theDescription, "The description cannot be blank.");
        Validate.notNull(theContents, "The contents cannot be null.");

        title = theTitle;
        description = theDescription;
        contents = new ArrayList<>(theContents);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Content> getContents() {
        return Collections.unmodifiableList(contents);
    }
}
