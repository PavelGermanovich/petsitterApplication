package com.germanovich.springboot.petsitterApp.enums;

public enum PetTypeEnum {
    DOG("Dog"),
    CAT("Cat");

    private String name;

    PetTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
