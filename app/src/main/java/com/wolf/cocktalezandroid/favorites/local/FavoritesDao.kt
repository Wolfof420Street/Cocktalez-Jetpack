package com.wolf.cocktalezandroid.favorites.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wolf.cocktalezandroid.common.util.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM ${Constants.TABLE_NAME} ORDER BY cocktailId DESC")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Query("SELECT * FROM ${Constants.TABLE_NAME} WHERE cocktailId == :cocktailId")
    fun getFavorite(cocktailId: Int): Flow<Favorite?>

    @Query("SELECT COUNT(1) > 0 FROM ${Constants.TABLE_NAME} WHERE cocktailId = :cocktailId")
    fun isFavorite(cocktailId: Int): Flow<Boolean>

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("DELETE FROM ${Constants.TABLE_NAME}")
    suspend fun deleteAllFavorites()
}