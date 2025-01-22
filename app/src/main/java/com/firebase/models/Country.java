package com.firebase.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Country implements Serializable {
    private String id;
    private String name;
    private String continent;
    private String imageUrl;
    private String comment;

    public Country() {
    }

    public Country(String name, String continent, String imageUrl, String comment) {
        this.name = name;
        this.continent = continent;
        this.imageUrl = imageUrl;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @NonNull
    @Override
    public String toString() {
        return "Country{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", continent='" + continent + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
