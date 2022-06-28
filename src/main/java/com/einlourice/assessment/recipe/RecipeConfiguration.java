package com.einlourice.assessment.recipe;

import com.einlourice.assessment.recipe.domain.Recipe;
import com.einlourice.assessment.recipe.dto.request.CreateRecipeRequest;
import com.einlourice.assessment.recipe.dto.request.UpdateRecipeRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecipeConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<CreateRecipeRequest, Recipe>() {
            @Override
            protected void configure() {
                skip(destination.getIngredientList());
                skip(destination.getInstructionList());
            }
        });
        modelMapper.addMappings(new PropertyMap<UpdateRecipeRequest, Recipe>() {
            @Override
            protected void configure() {
                skip(destination.getIngredientList());
                skip(destination.getInstructionList());
            }
        });

        return modelMapper;
    }
}
