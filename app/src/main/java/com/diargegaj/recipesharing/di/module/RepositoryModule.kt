package com.diargegaj.recipesharing.di.module

import com.diargegaj.recipesharing.data.repository.FirebaseImageUploadRepositoryImpl
import com.diargegaj.recipesharing.data.repository.UserInteractionRepositoryImpl
import com.diargegaj.recipesharing.data.repository.recipe.RecipeRepositoryImpl
import com.diargegaj.recipesharing.data.repository.userAuth.UserAuthRepositoryImpl
import com.diargegaj.recipesharing.data.repository.userFollow.UserFollowRepositoryImpl
import com.diargegaj.recipesharing.data.repository.userProfile.UserProfileRepositoryImpl
import com.diargegaj.recipesharing.domain.repository.ImageUploadRepository
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.repository.userInteraction.UserInteractionRepository
import com.diargegaj.recipesharing.domain.repository.userAuth.UserAuthRepository
import com.diargegaj.recipesharing.domain.repository.userFollow.UserFollowRepository
import com.diargegaj.recipesharing.domain.repository.userProfile.UserProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideUserRepository(
        userInteractionRepositoryImpl: UserInteractionRepositoryImpl
    ): UserInteractionRepository

    @Binds
    abstract fun provideImageUploadRepository(
        firebaseImageUploadRepositoryImpl: FirebaseImageUploadRepositoryImpl
    ): ImageUploadRepository

    @Binds
    abstract fun provideRecipeRepository(
        recipeRepositoryIml: RecipeRepositoryImpl
    ): RecipeRepository

    @Binds
    abstract fun provideUserAuthRepository(
        userAuthRepositoryImpl: UserAuthRepositoryImpl
    ): UserAuthRepository

    @Binds
    abstract fun provideUserProfileRepository(
        userProfileRepositoryImpl: UserProfileRepositoryImpl
    ): UserProfileRepository

    @Binds
    abstract fun provideUserFollowRepository(
        userFollowRepositoryImpl: UserFollowRepositoryImpl
    ): UserFollowRepository

}