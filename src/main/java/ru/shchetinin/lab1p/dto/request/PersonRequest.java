package ru.shchetinin.lab1p.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shchetinin.lab1p.entity.Color;
import ru.shchetinin.lab1p.entity.Country;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {

    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    private Color eyeColor;

    private Color hairColor;

    private Long location;

    @NotNull(message = "Birthday cannot be null")
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    private Date birthday;

    private Country nationality;
}
