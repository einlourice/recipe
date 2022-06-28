package com.einlourice.assessment.recipe.service;

import com.einlourice.assessment.recipe.domain.Ingredient;
import com.einlourice.assessment.recipe.domain.Instruction;
import com.einlourice.assessment.recipe.domain.Recipe;
import com.einlourice.assessment.recipe.dto.request.CreateRecipeRequest;
import com.einlourice.assessment.recipe.dto.request.GetRecipeListRequest;
import com.einlourice.assessment.recipe.dto.request.UpdateRecipeRequest;
import com.einlourice.assessment.recipe.dto.response.RecipeResponse;
import com.einlourice.assessment.recipe.exception.EntityNotFoundException;
import com.einlourice.assessment.recipe.query.RecipeSearchCriteria;
import com.einlourice.assessment.recipe.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.einlourice.assessment.recipe.repository.RecipeSpecifications.findByCriteria;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, ModelMapper modelMapper) {
        this.recipeRepository = recipeRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<RecipeResponse> findAll(GetRecipeListRequest getRecipeListRequest) {
        Assert.notNull(getRecipeListRequest, "getRecipeListRequest must not be null");

        RecipeSearchCriteria queryParam = modelMapper.map(getRecipeListRequest, RecipeSearchCriteria.class);

        List<Recipe> recipeList = recipeRepository.findAll(findByCriteria(queryParam));
        if (recipeList.isEmpty()) {
            return Collections.emptyList();
        } else {
            return recipeList.stream()
                    .map(r -> modelMapper.map(r, RecipeResponse.class))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public RecipeResponse create(CreateRecipeRequest createRecipeRequest) {
        Assert.notNull(createRecipeRequest, "createRecipeRequest must not be null");

        Recipe transientRecipe = modelMapper.map(createRecipeRequest, Recipe.class);
        createRecipeRequest.getInstructionList().forEach(t -> {
            Instruction instruction = modelMapper.map(t, Instruction.class);
            transientRecipe.addInstruction(instruction);
        });
        createRecipeRequest.getIngredientList().forEach(t -> {
            Ingredient ingredient = modelMapper.map(t, Ingredient.class);
            transientRecipe.addIngredient(ingredient);
        });

        Recipe recipe = recipeRepository.save(transientRecipe);
        return modelMapper.map(recipe, RecipeResponse.class);
    }

    @Transactional
    public RecipeResponse update(Long id, UpdateRecipeRequest updateRecipeRequest) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(updateRecipeRequest, "updateRecipeRequest must not be null");

        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipe.setName(updateRecipeRequest.getName());
            recipe.setNumberOfServings(updateRecipeRequest.getNumberOfServings());
            recipe.setIsVegetarian(updateRecipeRequest.getIsVegetarian());
            recipe.clear(); // remove all the children
            updateRecipeRequest.getInstructionList().forEach(t -> {
                Instruction instruction = modelMapper.map(t, Instruction.class);
                recipe.addInstruction(instruction);
            });
            updateRecipeRequest.getIngredientList().forEach(t -> {
                Ingredient ingredient = modelMapper.map(t, Ingredient.class);
                recipe.addIngredient(ingredient);
            });
            Recipe updatedRecipe = recipeRepository.save(recipe);

            return modelMapper.map(updatedRecipe, RecipeResponse.class);
        } else {
            throw new EntityNotFoundException("Recipe Not Found");
        }
    }

    @Transactional
    public void delete(Long id) {
        Assert.notNull(id, "id must not be null");
        try {
            recipeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Recipe Not Found");
        }
    }

}
