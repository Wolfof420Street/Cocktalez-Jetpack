package com.wolf.cocktalezandroid.favorites.domain.repository

import com.wolf.cocktalezandroid.favorites.local.Favorite
import kotlinx.coroutines.flow.Flow


interface FavoritesRepository {
    suspend fun insertFavorite(favorite: Favorite)
    fun getFavorites(): Flow<List<Favorite>>
    fun isFavorite(cocktailId: Int): Flow<Boolean>
    fun getAFavorites(mediaId: Int): Flow<Favorite?>
    suspend fun deleteOneFavorite(favorite: Favorite)
    suspend fun deleteAllFavorites()
}