package org.example.library.enums;

public enum RoleEnum {
    ADMIN("admin"), CLIENT("client");

    private final String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
