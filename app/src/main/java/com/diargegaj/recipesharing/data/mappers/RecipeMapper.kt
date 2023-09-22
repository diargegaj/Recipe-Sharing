package com.diargegaj.recipesharing.data.mappers

import com.diargegaj.recipesharing.data.db.entities.recipes.IngredientEntity
import com.diargegaj.recipesharing.data.db.entities.recipes.RecipeEntity
import com.diargegaj.recipesharing.data.db.entities.recipes.RecipeWithDetails
import com.diargegaj.recipesharing.data.models.RecipeDto
import com.diargegaj.recipesharing.domain.models.RecipeModel

fun RecipeModel.mapToDto(): RecipeDto {
    return RecipeDto(
        title = this.title,
        description = this.description,
        ingredients = this.ingredients,
        imageUrl = this.imageUrl,
        userId = this.userId
    )
}

fun RecipeWithDetails.mapToRecipeModel(): RecipeModel {
    return RecipeModel(
        recipeId = this.recipe.id,
        title = this.recipe.title,
        description = this.recipe.description,
        ingredients = this.ingredients.map { it.mapToIngredient() },
        imageUrl = this.recipe.imageUrl,
        userId = this.recipe.userId,
        userModel = this.user?.mapToUserModel()
    )
}

fun IngredientEntity.mapToIngredient() = this.ingredient

fun RecipeDto.toRecipeEntities(): Pair<RecipeEntity, List<IngredientEntity>> {
    val recipeEntity = RecipeEntity(
        id = this.recipeId,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        userId = this.userId
    )
    val ingredientEntities = this.ingredients.map {
        IngredientEntity(
            recipeId = this.recipeId,
            ingredient = it
        )
    }
    return recipeEntity to ingredientEntities
}
