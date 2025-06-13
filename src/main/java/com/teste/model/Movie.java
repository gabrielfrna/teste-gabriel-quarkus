package com.teste.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    private Integer movieYear;

    private String title;

    private String studios;

    private String producers;

    private Boolean winner;

}
