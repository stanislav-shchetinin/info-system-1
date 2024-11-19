package ru.shchetinin.lab1p.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Entity
@Table(name = "coordinates")
@Getter
@Setter
public class Coordinates extends RootEntity {
    private long x;

    @NotNull(message = "Y cannot be null")
    private Float y;
}