package com.example.moodplanet.Model;

public class CatMemes {
    String id;
    String created_at;
    String url;

    public CatMemes(String id, String created_at, String url) {
        this.id = id;
        this.created_at = created_at;
        this.url = url;
    }

    public CatMemes() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
