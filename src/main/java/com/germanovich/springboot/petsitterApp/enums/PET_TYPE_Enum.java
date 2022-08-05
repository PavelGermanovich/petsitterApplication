package com.germanovich.springboot.petsitterApp.enums;

public enum PET_TYPE_Enum {
    DOG("Dog"),
    CAT("Cat");

    private String name;

    PET_TYPE_Enum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
