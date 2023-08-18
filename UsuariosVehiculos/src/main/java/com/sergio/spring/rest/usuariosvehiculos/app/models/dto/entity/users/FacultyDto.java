package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users;

public class FacultyDto {

    private Long id;
    private String nameFaculty;

    public FacultyDto() {
    }

    public FacultyDto(Long id, String nameFaculty) {
        this.id = id;
        this.nameFaculty = nameFaculty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameFaculty() {
        return nameFaculty;
    }

    public void setNameFaculty(String nameFaculty) {
        this.nameFaculty = nameFaculty;
    }
}
