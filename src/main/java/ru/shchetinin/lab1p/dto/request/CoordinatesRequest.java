package ru.shchetinin.lab1p.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesRequest {

    private long x;

    @NotNull(message = "Y cannot be null")
    private Float y;
}
