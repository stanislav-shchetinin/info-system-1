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
public class Location extends RootEntity {

    @NotNull
    @Column(name = "x")
    private Double x;

    @NotNull
    @Column(name = "y")
    private Long y;

    @NotNull
    @Column(name = "z")
    private Float z;
}
