package com.einlourice.assessment.recipe.controller;


import com.einlourice.assessment.recipe.RecipeApplication;
import com.einlourice.assessment.recipe.domain.Ingredient;
import com.einlourice.assessment.recipe.domain.Instruction;
import com.einlourice.assessment.recipe.domain.Recipe;
import com.einlourice.assessment.recipe.dto.request.IngredientRequest;
import com.einlourice.assessment.recipe.dto.request.InstructionRequest;
import com.einlourice.assessment.recipe.dto.request.RecipeRequest;
import com.einlourice.assessment.recipe.repository.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RecipeApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integration-test.properties")
public class RecipeControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private RecipeRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() throws Exception {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "INGREDIENT", "INSTRUCTION", "RECIPE");
    }

    @Test
    public void create_HappyScenario_OK()
            throws Exception {

        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRecipeRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("test")))
                .andExpect(jsonPath("$.isVegetarian", is(false)))
                .andExpect(jsonPath("$.numberOfServings", is(4)));
    }

    @Test
    public void create_EmptyName_4xxClientError()
            throws Exception {
        RecipeRequest recipeRequest = createRecipeRequest();
        recipeRequest.setName("");
        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void create_NullName_4xxClientError()
            throws Exception {
        RecipeRequest recipeRequest = createRecipeRequest();
        recipeRequest.setName(null);
        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void create_NullIsVegetarian_4xxClientError()
            throws Exception {
        RecipeRequest recipeRequest = createRecipeRequest();
        recipeRequest.setIsVegetarian(null);
        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void create_NullNumberOfServings_4xxClientError()
            throws Exception {
        RecipeRequest recipeRequest = createRecipeRequest();
        recipeRequest.setNumberOfServings(null);
        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void create_EmptyInstructionList_4xxClientError()
            throws Exception {
        RecipeRequest recipeRequest = createRecipeRequest();
        recipeRequest.setInstructionList(new ArrayList<>());
        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void create_EmptyIngredientList_4xxClientError()
            throws Exception {
        RecipeRequest recipeRequest = createRecipeRequest();
        recipeRequest.setIngredientList(new ArrayList<>());
        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeRequest)))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void update_HappyScenario_OK()
            throws Exception {
        Recipe recipe = createDummyRecipe();

        mockMvc.perform(put("/api/recipe/" + recipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRecipeRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("test")))
                .andExpect(jsonPath("$.isVegetarian", is(false)))
                .andExpect(jsonPath("$.numberOfServings", is(4)));
    }

    @Test
    public void get_HappyScenario_OK()
            throws Exception {
        createDummyRecipe();

        mockMvc.perform(get("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("test")))
                .andExpect(jsonPath("$[0].isVegetarian", is(true)))
                .andExpect(jsonPath("$[0].numberOfServings", is(5)));

        mockMvc.perform(get("/api/recipe?isVegetarian=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("test")))
                .andExpect(jsonPath("$[0].isVegetarian", is(true)))
                .andExpect(jsonPath("$[0].numberOfServings", is(5)));

        mockMvc.perform(get("/api/recipe?numberOfServings=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("test")))
                .andExpect(jsonPath("$[0].isVegetarian", is(true)))
                .andExpect(jsonPath("$[0].numberOfServings", is(5)));

        mockMvc.perform(get("/api/recipe?ingredientList=ing-test&ingredientFilterType=include")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("test")))
                .andExpect(jsonPath("$[0].isVegetarian", is(true)))
                .andExpect(jsonPath("$[0].numberOfServings", is(5)));

        mockMvc.perform(get("/api/recipe?ingredientList=notexisting&ingredientFilterType=include")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        mockMvc.perform(get("/api/recipe?instruction=ins-test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("test")))
                .andExpect(jsonPath("$[0].isVegetarian", is(true)))
                .andExpect(jsonPath("$[0].numberOfServings", is(5)));
    }

    @Test
    public void delete_HappyScenario_OK()
            throws Exception {
        Recipe recipe = createDummyRecipe();

        mockMvc.perform(get("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("test")))
                .andExpect(jsonPath("$[0].isVegetarian", is(true)))
                .andExpect(jsonPath("$[0].numberOfServings", is(5)));

        mockMvc.perform(delete("/api/recipe/" + recipe.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    private Recipe createDummyRecipe() {
        Recipe recipe = new Recipe();
        recipe.setName("test");
        recipe.setIsVegetarian(true);
        recipe.setNumberOfServings(5);

        Ingredient ingredient = new Ingredient();
        ingredient.setName("ing-test");
        Instruction instruction = new Instruction();
        instruction.setStep("ins-test");

        recipe.addIngredient(ingredient);
        recipe.addInstruction(instruction);

        return repository.saveAndFlush(recipe);
    }

    private RecipeRequest createRecipeRequest() {
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setName("test");
        recipeRequest.setIsVegetarian(false);
        recipeRequest.setNumberOfServings(4);

        IngredientRequest ingredient = new IngredientRequest();
        ingredient.setName("ing-test");
        InstructionRequest instruction = new InstructionRequest();
        instruction.setStep("ins-test");

        recipeRequest.setIngredientList(new ArrayList<>() {{
            add(ingredient);
        }});
        recipeRequest.setInstructionList(new ArrayList<>() {{
            add(instruction);
        }});

        return recipeRequest;
    }


}
