package com.einlourice.assessment.recipe.service;

import com.einlourice.assessment.recipe.domain.Ingredient;
import com.einlourice.assessment.recipe.domain.Instruction;
import com.einlourice.assessment.recipe.domain.Recipe;
import com.einlourice.assessment.recipe.dto.request.CreateRecipeRequest;
import com.einlourice.assessment.recipe.dto.request.IngredientRequest;
import com.einlourice.assessment.recipe.dto.request.InstructionRequest;
import com.einlourice.assessment.recipe.dto.request.UpdateRecipeRequest;
import com.einlourice.assessment.recipe.dto.response.RecipeResponse;
import com.einlourice.assessment.recipe.exception.EntityNotFoundException;
import com.einlourice.assessment.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepositoryMock;
    @Mock
    private ModelMapper modelMapperMock;

    private RecipeService recipeService;

    @BeforeEach
    public void init() {
        recipeService = new RecipeService(recipeRepositoryMock, modelMapperMock);
    }

    @Test
    @DisplayName("Test create method with happy scenario, expected OK")
    public void create_HappyScenario_OK() {
        recipeService = new RecipeService(recipeRepositoryMock, new ModelMapper());

        CreateRecipeRequest createRecipeRequest = buildCreateRecipeRequest();
        Recipe recipe = buildRecipe();
        when(recipeRepositoryMock.save(any())).thenReturn(recipe);

        RecipeResponse recipeResponse = recipeService.create(createRecipeRequest);

        assertAll(() -> assertEquals(recipe.getName(), recipeResponse.getName()),
                () -> assertEquals(recipe.getIsVegetarian(), recipeResponse.getIsVegetarian()),
                () -> assertEquals(recipe.getNumberOfServings(), recipeResponse.getNumberOfServings()),
                () -> assertEquals(recipe.getIngredientList().size(), recipeResponse.getIngredientList().size()),
                () -> assertEquals(recipe.getInstructionList().size(), recipeResponse.getInstructionList().size()));
    }

    @Test
    @DisplayName("Test create method with null input, expected not OK")
    public void create_ThrowsException_Error() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.create(null));
    }

    @Test
    @DisplayName("Test update method with valid inputs, check repository.save been invoked. expected OK")
    public void update_CheckIfSaveInvoked_OK() {
        when(recipeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(new Recipe()));
        when(modelMapperMock.map(any(InstructionRequest.class), any())).thenReturn(new Instruction());
        when(modelMapperMock.map(any(IngredientRequest.class), any())).thenReturn(new Ingredient());

        UpdateRecipeRequest updateRecipeRequest = buildUpdateRecipeRequest();
        recipeService.update(1L, updateRecipeRequest);

        verify(recipeRepositoryMock, times(1)).save(any(Recipe.class));
    }

    @Test
    @DisplayName("Test update method with optional not present, check repository.save been invoked. expected not OK")
    public void update_CheckIfSaveInvoked_Error() {
        when(recipeRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        UpdateRecipeRequest updateRecipeRequest = buildUpdateRecipeRequest();

        assertThrows(EntityNotFoundException.class, () -> recipeService.update(1L, updateRecipeRequest));
    }

    @Test
    @DisplayName("Test update method with happy scenario, expected OK")
    public void update_HappyScenario_OK() {
        Recipe recipe = new Recipe();
        UpdateRecipeRequest updateRecipeRequest = buildUpdateRecipeRequest();
        recipeService = new RecipeService(recipeRepositoryMock, new ModelMapper());

        when(recipeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(recipe));
        when(recipeRepositoryMock.save(any())).thenReturn(recipe);


        RecipeResponse recipeResponse = recipeService.update(1L, updateRecipeRequest);

        assertAll(() -> assertEquals(recipe.getName(), recipeResponse.getName()),
                () -> assertEquals(recipe.getIsVegetarian(), recipeResponse.getIsVegetarian()),
                () -> assertEquals(recipe.getNumberOfServings(), recipeResponse.getNumberOfServings()),
                () -> assertEquals(recipe.getIngredientList().size(), recipeResponse.getIngredientList().size()),
                () -> assertEquals(recipe.getInstructionList().size(), recipeResponse.getInstructionList().size()));
    }

    @Test
    @DisplayName("Test update method with null inputs, expected not OK")
    public void update_NullInput_Error() {
        UpdateRecipeRequest updateRecipeRequest = buildUpdateRecipeRequest();

        assertThrows(IllegalArgumentException.class, () -> recipeService.update(null, updateRecipeRequest));
        assertThrows(IllegalArgumentException.class, () -> recipeService.update(1L, null));
    }

    @Test
    @DisplayName("Test delete method with happy scenario, expected OK")
    public void delete_HappyScenario_OK() {
        recipeService.delete(1L);
        verify(recipeRepositoryMock, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Test delete method with null inputs, expected not OK")
    public void delete_NullInput_Error() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.delete(null));
    }


    private Recipe buildRecipe() {
        Recipe recipe = new Recipe();
        recipe.setName("test");
        recipe.setIsVegetarian(true);
        recipe.setNumberOfServings(1);

        List<Ingredient> ingredientRequestList = new ArrayList<>() {{
            add(new Ingredient("ingredient-1"));
            add(new Ingredient("ingredient-2"));
        }};
        List<Instruction> instructionList = new ArrayList<>() {{
            add(new Instruction("instruction-1"));
            add(new Instruction("instruction-2"));
        }};

        recipe.setIngredientList(ingredientRequestList);
        recipe.setInstructionList(instructionList);

        return recipe;
    }

    private CreateRecipeRequest buildCreateRecipeRequest() {
        CreateRecipeRequest createRecipeRequest = new CreateRecipeRequest();
        createRecipeRequest.setName("test");
        createRecipeRequest.setIsVegetarian(true);
        createRecipeRequest.setNumberOfServings(1);

        List<IngredientRequest> ingredientRequestList = new ArrayList<>() {{
            add(new IngredientRequest("ingredient-req-1"));
            add(new IngredientRequest("ingredient-req-2"));
        }};
        List<InstructionRequest> instructionList = new ArrayList<>() {{
            add(new InstructionRequest("instruction-req-1"));
            add(new InstructionRequest("instruction-req-2"));
        }};

        createRecipeRequest.setIngredientList(ingredientRequestList);
        createRecipeRequest.setInstructionList(instructionList);

        return createRecipeRequest;
    }

    private UpdateRecipeRequest buildUpdateRecipeRequest() {
        UpdateRecipeRequest updateRecipeRequest = new UpdateRecipeRequest();
        updateRecipeRequest.setName("test");
        updateRecipeRequest.setIsVegetarian(true);
        updateRecipeRequest.setNumberOfServings(1);

        List<IngredientRequest> ingredientRequestList = new ArrayList<>() {{
            add(new IngredientRequest("ingredient-req-1"));
        }};
        List<InstructionRequest> instructionList = new ArrayList<>() {{
            add(new InstructionRequest("instruction-req-1"));
        }};

        updateRecipeRequest.setIngredientList(ingredientRequestList);
        updateRecipeRequest.setInstructionList(instructionList);

        return updateRecipeRequest;
    }

}
