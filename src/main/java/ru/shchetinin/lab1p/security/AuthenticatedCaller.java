package ru.shchetinin.lab1p.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@RequestScoped
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedCaller {

    private Long id;

    private Set<String> roles;

    private boolean authenticated;

}
