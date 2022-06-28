package com.einlourice.assessment.recipe.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "INSTRUCTION")
public class Instruction {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "RECIPE_ID")
    private Recipe recipe;

    @Column(name = "STEP", nullable = false)
    private String step;

    public Instruction(String step) {
        this.step = step;
    }
}
