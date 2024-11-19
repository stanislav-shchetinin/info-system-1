package ru.shchetinin.lab1p.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shchetinin.lab1p.entity.MovieGenre;
import ru.shchetinin.lab1p.entity.MpaaRating;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {

    @NotNull(message = "Movie name cannot be null")
    @NotEmpty(message = "Movie name cannot be empty")
    private String name;

    @NotNull(message = "Coordinates cannot be null")
    private Long coordinates;

    @Min(value = 1, message = "Oscar count must be greater than 0")
    private Long oscarsCount;

    @Min(value = 0, message = "Budget must be greater than 0")
    private double budget;

    @Min(value = 0, message = "Total box office must be greater than 0")
    private double totalBoxOffice;

    private MpaaRating mpaaRating;

    private Long director;

    @NotNull(message = "Screenwriter cannot be null")
    private Long screenwriter;

    private Long operator;

    @Min(value = 1, message = "Length must be greater than 0")
    private int length;

    @Min(value = 1, message = "Golden Palm count must be greater than 0")
    private Long goldenPalmCount;

    @Size(max = 172, message = "Tagline cannot exceed 172 characters")
    private String tagline;

    @NotNull
    private MovieGenre genre;
}
