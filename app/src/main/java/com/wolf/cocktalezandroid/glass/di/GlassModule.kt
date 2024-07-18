package com.wolf.cocktalezandroid.glass.di

import com.wolf.cocktalezandroid.common.data.network.CocktailDbApi
import com.wolf.cocktalezandroid.home.data.repository.CocktailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GlassModule {

    @Provides
    @Singleton
    fun provideCocktailRepository(api: CocktailDbApi) = CocktailsRepository(api)

}