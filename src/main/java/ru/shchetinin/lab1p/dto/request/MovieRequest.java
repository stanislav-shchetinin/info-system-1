package ru.shchetinin.lab1p.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shchetinin.lab1p.entity.MovieGenre;
import ru.shchetinin.lab1p.entity.MpaaRating;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {

    @NotEmpty
    private String name;

    @NotNull
    private Long coordinates;

    @Min(value = 1)
    private long oscarsCount;

    @Min(value = 0)
    private Double budget;

    @Min(value = 0)
    private float totalBoxOffice;

    private MpaaRating mpaaRating;

    private Long director;

    private Long screenwriter;

    private Long operator;

    @Min(value = 1)
    private int length;

    @Min(value = 1)
    private Integer goldenPalmCount;

    @NotEmpty
    private String tagline;

    @NotNull
    private MovieGenre genre;
}
