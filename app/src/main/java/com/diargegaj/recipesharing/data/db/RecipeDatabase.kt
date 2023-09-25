package com.diargegaj.recipesharing.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.diargegaj.recipesharing.data.db.dao.FeedbackDao
import com.diargegaj.recipesharing.data.db.dao.RecipeDao
import com.diargegaj.recipesharing.data.db.dao.UserDao
import com.diargegaj.recipesharing.data.db.entities.user.UserEntity
import com.diargegaj.recipesharing.data.db.entities.feedback.FeedbackEntity
import com.diargegaj.recipesharing.data.db.entities.recipes.IngredientEntity
import com.diargegaj.recipesharing.data.db.entities.recipes.RecipeEntity

@Database(
    entities = [UserEntity::class, RecipeEntity::class, IngredientEntity::class, FeedbackEntity::class],
    version = 1
)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun recipeDao(): RecipeDao

    abstract fun feedbackDao(): FeedbackDao

}