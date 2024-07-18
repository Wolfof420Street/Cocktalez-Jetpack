package com.wolf.cocktalezandroid.favorites.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wolf.cocktalezandroid.common.util.Constants.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Favorite(
    @PrimaryKey val cocktailId: Int,
    val name: String,
    val image: String,
    val category: String,
    val alcoholic: String,
    val glass: String,
    val instructions: String,
    val ingredients: String,
    val measures: String,
)