package com.seb.model;

public class User {
    private Long id;
    private String username;
    private String password;
    private int eloScore;
    private String name;
    private String bio;
    private String image;

    public User() {
    }

    public User(Long id, String username, String password, int eloScore) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.eloScore = eloScore;
    }

    public User(Long id, String username, String password, int eloScore, String name, String bio, String image) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.eloScore = eloScore;
        this.name = name;
        this.bio = bio;
        this.image = image;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getEloScore() {
        return eloScore;
    }
    public void setEloScore(int eloScore) {
        this.eloScore = eloScore;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
