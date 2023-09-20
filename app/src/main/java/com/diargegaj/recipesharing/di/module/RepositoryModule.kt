package com.diargegaj.recipesharing.di.module

import com.diargegaj.recipesharing.data.repository.FirebaseImageUploadRepositoryImpl
import com.diargegaj.recipesharing.data.repository.RecipeRepositoryImpl
import com.diargegaj.recipesharing.data.repository.UserRepositoryImpl
import com.diargegaj.recipesharing.domain.repository.ImageUploadRepository
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun provideImageUploadRepository(
        firebaseImageUploadRepositoryImpl: FirebaseImageUploadRepositoryImpl
    ): ImageUploadRepository

    @Binds
    abstract fun provideRecipeRepository(
        recipeRepositoryIml: RecipeRepositoryImpl
    ): RecipeRepository

}