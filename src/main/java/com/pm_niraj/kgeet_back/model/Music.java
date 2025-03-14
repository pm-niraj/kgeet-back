package com.pm_niraj.kgeet_back.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

@Entity
public class Music {
    public Music() {
        this(null, null);
    }
    public Music(String title, String url){
        this.title = title;
        this.url = url;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Integer id;

    @Column
    private String title;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Column
    private String url;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
