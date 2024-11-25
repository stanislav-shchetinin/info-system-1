package ru.shchetinin.lab1p.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Person extends SubjectAreaEntity {

    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "eye_color")
    private Color eyeColor;

    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color")
    private Color hairColor;

    @ManyToOne
    @JoinColumn(
            name = "location_id",
            referencedColumnName = "id"
    )
    private Location location;

    @OneToMany(mappedBy = "director")
    @JsonIgnore
    private List<Movie> directorMovies;

    @OneToMany(mappedBy = "operator")
    @JsonIgnore
    private List<Movie> operatorMovies;

    @OneToMany(mappedBy = "screenwriter")
    @JsonIgnore
    private List<Movie> screenwriterMovies;

    @NotNull(message = "Birthday cannot be null")
    @Column(name = "birthday", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private Date birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "nationality")
    private Country nationality;
}
