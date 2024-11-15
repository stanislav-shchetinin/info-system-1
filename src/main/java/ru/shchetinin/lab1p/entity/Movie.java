package ru.shchetinin.lab1p.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "movie")
@Getter
@Setter
public class Movie extends RootEntity {

    @NotEmpty
    private String name;

    @ManyToOne
    @JoinColumn(
            name = "coordinates_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private Coordinates coordinates;

    @Min(value = 1)
    @Column(name = "oscars_count")
    private long oscarsCount;

    @Min(value = 0)
    @Column(name = "budget")
    private Double budget;

    @Min(value = 0)
    @Column(name = "total_box_office")
    private float totalBoxOffice;

    @Enumerated(EnumType.STRING)
    @Column(name = "mpaa_rating")
    private MpaaRating mpaaRating;

    @ManyToOne
    @JoinColumn(name = "director_id", referencedColumnName = "id")
    private Person director;

    @ManyToOne
    @JoinColumn(name = "screenwriter_id", referencedColumnName = "id")
    private Person screenwriter;

    @ManyToOne
    @JoinColumn(name = "operator_id", referencedColumnName = "id")
    private Person operator;

    @Min(value = 1)
    private int length;

    @Min(value = 1)
    @Column(name = "golden_palm_count")
    private Integer goldenPalmCount;

    @NotEmpty
    @Column(name = "tag_line")
    private String tagline;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MovieGenre genre;
}
