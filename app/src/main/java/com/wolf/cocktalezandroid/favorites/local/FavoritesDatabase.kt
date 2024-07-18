package com.wolf.cocktalezandroid.favorites.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Favorite::class], version = 5, exportSchema = true)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract val dao: FavoriteDao
}