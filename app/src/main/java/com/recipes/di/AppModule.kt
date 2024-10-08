package com.recipes.di

import android.content.Context
import com.recipes.data.RecipesRepositoryImpl
import com.recipes.data.SettingsRepositoryImpl
import com.recipes.domain.repository.RecipesRepository
import com.recipes.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRecipesRepository(
        @ApplicationContext context: Context
    ): RecipesRepository {
        return RecipesRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        @ApplicationContext context: Context
    ): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
}