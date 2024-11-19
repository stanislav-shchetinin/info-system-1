package ru.shchetinin.lab1p.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequest {

    @NotNull(message = "X cannot be null")
    private Double x;

    @NotNull(message = "Y cannot be null")
    private Long y;

    @NotNull(message = "Z cannot be null")
    private Float z;
}
