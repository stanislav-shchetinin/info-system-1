package ru.shchetinin.lab1p.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class SubjectAreaEntity extends  RootEntity {
    @Column(
            name = "creator",
            nullable = false,
            updatable = false
    )
    private Long creator;

    

}
