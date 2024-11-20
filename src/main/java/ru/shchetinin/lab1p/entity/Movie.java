package ru.shchetinin.lab1p.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "movie")
@Getter
@Setter
public class Movie extends RootEntity {

    @NotNull(message = "Movie name cannot be null")
    @NotEmpty(message = "Movie name cannot be empty")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "Coordinates cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "coordinates_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private Coordinates coordinates;

//    @Column(name = "creation_date", nullable = false, updatable = false)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime creationDate;

    @Min(value = 1, message = "Oscar count must be greater than 0")
    @Column(name = "oscars_count")
    private Long oscarsCount;

    @Min(value = 0, message = "Budget must be greater than 0")
    @Column(name = "budget", nullable = false)
    private double budget;

    @Min(value = 0, message = "Total box office must be greater than 0")
    @Column(name = "total_box_office", nullable = false)
    private double totalBoxOffice;

    @Enumerated(EnumType.STRING)
    @Column(name = "mpaa_rating")
    private MpaaRating mpaaRating;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "director_id", referencedColumnName = "id")
    private Person director;

    @NotNull(message = "Screenwriter cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "screenwriter_id", referencedColumnName = "id")
    private Person screenwriter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id", referencedColumnName = "id")
    private Person operator;

    @Min(value = 1, message = "Length must be greater than 0")
    @Column(name = "length", nullable = false)
    private int length;

    @Min(value = 1, message = "Golden Palm count must be greater than 0")
    @Column(name = "golden_palm_count")
    private Long goldenPalmCount;

    @Min(value = 1, message = "USA box office must be greater than 0")
    @Column(name = "usa_box_office")
    private Long usaBoxOffice;

    @Size(max = 172, message = "Tagline cannot exceed 172 characters")
    @Column(name = "tag_line", length = 172)
    private String tagline;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private MovieGenre genre;
}
