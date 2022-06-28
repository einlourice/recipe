package com.einlourice.assessment.recipe.controller;

import com.einlourice.assessment.recipe.dto.request.CreateRecipeRequest;
import com.einlourice.assessment.recipe.dto.request.GetRecipeListRequest;
import com.einlourice.assessment.recipe.dto.request.UpdateRecipeRequest;
import com.einlourice.assessment.recipe.dto.response.RecipeResponse;
import com.einlourice.assessment.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "List all persisted recipe",
            description = "List all persisted recipe from the database, including associated ingredients and instructions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request, validation failed due to unexpected payload")
    })
    public List<RecipeResponse> list(@Valid GetRecipeListRequest getRecipeListRequest) {
        return recipeService.findAll(getRecipeListRequest);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new recipe",
            description = "Create a new recipe, together with ingredients and instruction in a structured JSON")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request, validation failed due to unexpected payload")
    })
    public RecipeResponse create(@Valid @RequestBody CreateRecipeRequest createRecipeRequest) {
        return recipeService.create(createRecipeRequest);
    }


    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update the existing recipe",
            description = "Update the recipe's properties, and replace all associated ingredients and instructions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request, validation failed due to unexpected payload"),
            @ApiResponse(responseCode = "404", description = "Recipe does not exist"),
    })
    public RecipeResponse update(@PathVariable Long id, @Valid @RequestBody UpdateRecipeRequest updateRecipeRequest) {
        return recipeService.update(id, updateRecipeRequest);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete an existing recipe",
            description = "Delete a recipe using an ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request, validation failed due to unexpected payload"),
            @ApiResponse(responseCode = "404", description = "Recipe does not exist")
    })
    public void delete(@PathVariable Long id) {
        recipeService.delete(id);
    }

}
