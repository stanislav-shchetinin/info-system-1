package org.example.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class Coordinates implements Serializable {
    private long x;

    @Column(nullable = false)
    private Float y;
}