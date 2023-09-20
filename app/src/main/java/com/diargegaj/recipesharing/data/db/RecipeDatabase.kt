package com.diargegaj.recipesharing.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.diargegaj.recipesharing.data.db.dao.UserDao
import com.diargegaj.recipesharing.data.db.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}