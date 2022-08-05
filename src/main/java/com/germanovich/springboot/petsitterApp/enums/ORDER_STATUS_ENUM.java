package com.germanovich.springboot.petsitterApp.enums;

public enum ORDER_STATUS_ENUM {
    DRAFT("Draft"),
    SUBMITTED("Submitted"),
    APPROVED("Approved"),
    DECLINED("Declined");

    private String name;

    ORDER_STATUS_ENUM(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
