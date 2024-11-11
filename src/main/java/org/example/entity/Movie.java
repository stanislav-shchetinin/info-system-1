package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Embedded
    private Coordinates coordinates;

    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    private Long oscarsCount;

    @Column(nullable = false)
    private double budget;

    @Column(nullable = false)
    private double totalBoxOffice;

    @Enumerated(EnumType.STRING)
    private MpaaRating mpaaRating;

    @ManyToOne
    private Person director;

    @ManyToOne
    private Person screenwriter;

    @ManyToOne
    private Person operator;

    @Column(nullable = false)
    private int length;

    private Long goldenPalmCount;

    private Long usaBoxOffice;

    @Column(length = 172)
    private String tagline;

    @Enumerated(EnumType.STRING)
    private MovieGenre genre;
}
