package com.mp.admin.domain;

import org.apache.commons.lang3.Validate;

import javax.persistence.*;

@Entity
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ContentType contentType;

    @Column(nullable = false)
    private String url;

    Content() {
    }

    public Content(String theTitle, ContentType theContentType, String theUrl) {
        Validate.notBlank(theTitle, "The title cannot be blank.");
        Validate.notNull(theContentType, "The content type cannot be null.");
        Validate.notBlank(theUrl, "The url cannot be blank.");

        this.title = theTitle;
        this.contentType = theContentType;
        this.url = theUrl;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public String getUrl() {
        return url;
    }
}
