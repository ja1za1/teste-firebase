package com.firebase.models;

import java.io.Serializable;

public class Country implements Serializable {
    private String id;          // ID do documento no Firestore
    private String name;        // Nome do país
    private String continent;   // Continente do país
    private String imageUrl;    // URL da imagem do país
    private String comment;     // Comentário (opcional)

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
