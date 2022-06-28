package com.einlourice.assessment.recipe.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "INGREDIENT")
public class Ingredient {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "RECIPE_ID")
    private Recipe recipe;
    
    @Column(name = "NAME", nullable = false)
    private String name;


    public Ingredient(String name) {
        this.name = name;
    }
}
