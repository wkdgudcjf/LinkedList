package com.ep.linkedlist.model.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Profile {
    @JsonIgnore
    String uid;
    String name;
    String nickname;
    String gender;
    int age;
    int birthYear;
    int birthMonth;
    int birthDay;
    String bloodType;
    int height;
    String photoURI;
    String bodyType;
    String religion;
    String region;

    public Profile() {
        this.name = "";
        this.nickname = "";
        this.gender = null;
        this.age = 0;
        this.birthYear = 0;
        this.birthMonth = 0;
        this.birthDay = 0;
        this.bloodType = null;
        this.height = 0;
        this.photoURI = null;
        this.bodyType = null;
        this.religion = null;
        this.region = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() { return gender; }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getBirthYear() { return birthYear; }

    public void setBirthYear(int birthYear) { this.birthYear = birthYear; }

    public int getBirthMonth() { return birthMonth; }

    public void setBirthMonth(int birthMonth) { this.birthMonth = birthMonth; }

    public int getBirthDay() { return birthDay; }

    public void setBirthDay(int birthDay) { this.birthDay = birthDay; }

    public String getBloodType() { return bloodType; }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }

    public String getPhotoURI() { return photoURI; }

    public void setPhotoURI(String photoURI) { this.photoURI = photoURI; }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
