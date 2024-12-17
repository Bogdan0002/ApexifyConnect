package com.apexify.logic.DTO;


public class TagRequestDTO {


    private String name;

    // Constructor
    public TagRequestDTO() {}

    public TagRequestDTO(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}