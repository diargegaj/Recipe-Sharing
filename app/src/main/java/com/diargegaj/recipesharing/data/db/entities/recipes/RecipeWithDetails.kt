package com.diargegaj.recipesharing.data.db.entities.recipes

import androidx.room.Embedded
import androidx.room.Relation
import com.diargegaj.recipesharing.data.db.entities.user.UserEntity

data class RecipeWithDetails(
    @Embedded val recipe: RecipeEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId",
    )
    val ingredients: List<IngredientEntity>,

    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val user: UserEntity?
)