package com.germanovich.springboot.petsitterApp.enums;

public enum USER_ROLE {
    PET_OWNER("PET_OWNER"),
    PET_SITTER("PET_SITTER");

    private String roleId;

    USER_ROLE(String name) {
        this.roleId = name;
    }

    public String getRoleId() {
        return roleId;
    }
}
