package com.germanovich.springboot.petsitterApp.enums;

public enum PETSITTER_SERVICE {
    SITTING("petsitting"),
    WALKING("dog walking");

    private String serviceName;

    PETSITTER_SERVICE(String name) {
        this.serviceName = name;
    }

    public String getRoleName() {
        return serviceName;
    }
}
