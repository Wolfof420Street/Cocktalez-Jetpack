package com.wolf.cocktalezandroid.favorites.di


import com.wolf.cocktalezandroid.favorites.data.local.FavoritesRepositoryImpl
import com.wolf.cocktalezandroid.favorites.domain.repository.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoritesModule {
    @Binds
    abstract fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ): FavoritesRepository
}