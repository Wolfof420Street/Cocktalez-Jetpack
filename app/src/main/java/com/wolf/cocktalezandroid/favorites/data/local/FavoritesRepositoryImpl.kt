package com.wolf.cocktalezandroid.favorites.data.local

import com.wolf.cocktalezandroid.favorites.domain.repository.FavoritesRepository
import com.wolf.cocktalezandroid.favorites.local.Favorite
import com.wolf.cocktalezandroid.favorites.local.FavoritesDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(private val database: FavoritesDatabase):
    FavoritesRepository {
    override suspend fun insertFavorite(favorite: Favorite) {
        database.dao.insertFavorite(favorite)
    }

    override fun getFavorites(): Flow<List<Favorite>> {
        return database.dao.getAllFavorites()
    }

    override fun isFavorite(cocktailId: Int): Flow<Boolean>{
        return database.dao.isFavorite(cocktailId)
    }

    override fun getAFavorites(cocktailId: Int): Flow<Favorite?> {
        return database.dao.getFavorite(cocktailId)
    }

    override suspend fun deleteOneFavorite(favorite: Favorite) {
        database.dao.deleteFavorite(favorite)
    }

    override suspend fun deleteAllFavorites() {
        database.dao.deleteAllFavorites()
    }
}