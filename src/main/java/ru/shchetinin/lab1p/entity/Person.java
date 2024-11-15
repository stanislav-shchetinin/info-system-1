package ru.shchetinin.lab1p.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
public class Person extends RootEntity {

    @NotEmpty
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "eye_color")
    private Color eyeColor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color")
    private Color hairColor;

    @ManyToOne
    @JoinColumn(
            name = "location_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Location location;

    @OneToMany(mappedBy = "director")
    private List<Movie> directorMovies;

    @OneToMany(mappedBy = "operator")
    private List<Movie> operatorMovies;

    @OneToMany(mappedBy = "screenwriter")
    private List<Movie> screenwriterMovies;

    @NotNull
    private Date birthday;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Country nationality;
}
