package com.einlourice.assessment.recipe.repository;

import com.einlourice.assessment.recipe.constant.IngredientFilterType;
import com.einlourice.assessment.recipe.domain.Ingredient;
import com.einlourice.assessment.recipe.domain.Instruction;
import com.einlourice.assessment.recipe.domain.Recipe;
import com.einlourice.assessment.recipe.query.RecipeSearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


public class RecipeSpecifications {
    public static Specification<Recipe> findByCriteria(RecipeSearchCriteria recipeSearchCriteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (recipeSearchCriteria.getIsVegetarian() != null) {
                predicates.add(cb.equal(root.get("isVegetarian"), recipeSearchCriteria.getIsVegetarian()));
            }
            if (recipeSearchCriteria.getNumberOfServings() != null) {
                predicates.add(cb.equal(root.get("numberOfServings"), recipeSearchCriteria.getNumberOfServings()));
            }
            if (StringUtils.isNotEmpty(recipeSearchCriteria.getInstruction())) {
                Join<Recipe, Instruction> instructionJoin = root.join("instructionList", JoinType.INNER);
                predicates.add(cb.like(instructionJoin.get("step"), "%" + recipeSearchCriteria.getInstruction() + "%"));
            }
            if (!CollectionUtils.isEmpty(recipeSearchCriteria.getIngredientList())) {
                Join<Recipe, Ingredient> ingredientJoin = root.join("ingredientList", JoinType.INNER);

                if (StringUtils.equalsAnyIgnoreCase(IngredientFilterType.EXCLUDE.name(), recipeSearchCriteria.getIngredientFilterType())) {
                    Subquery<Ingredient> sqIngredient = query.subquery(Ingredient.class);
                    Root<Ingredient> sqIngredientRoot = sqIngredient.from(Ingredient.class);
                    sqIngredient.select(sqIngredientRoot.get("recipe"))
                            .where(sqIngredientRoot.get("name").in(recipeSearchCriteria.getIngredientList()));

                    predicates.add(root.get("id").in(sqIngredient).not());
                } else {
                    predicates.add(ingredientJoin.get("name").in(recipeSearchCriteria.getIngredientList()));
                }
            }

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}


