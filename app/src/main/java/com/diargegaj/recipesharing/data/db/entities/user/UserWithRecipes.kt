package com.diargegaj.recipesharing.data.db.entities.user

import androidx.room.Embedded
import androidx.room.Relation
import com.diargegaj.recipesharing.data.db.entities.recipes.RecipeEntity

data class UserWithRecipes(
    @Embedded val user: UserEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val recipes: List<RecipeEntity>?
)
