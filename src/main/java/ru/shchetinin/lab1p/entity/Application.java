package ru.shchetinin.lab1p.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "application")
public class Application extends SubjectAreaEntity {

}
