package org.example.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Long y;

    @Column(nullable = false)
    private Float z;
}
