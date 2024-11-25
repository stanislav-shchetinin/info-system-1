package ru.shchetinin.lab1p.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "location")
@Getter
@Setter
public class Location extends SubjectAreaEntity {

    @NotNull(message = "X cannot be null")
    @Column(name = "x")
    private Double x;

    @NotNull(message = "Y cannot be null")
    @Column(name = "y")
    private Long y;

    @NotNull(message = "Z cannot be null")
    @Column(name = "z")
    private Float z;
}
