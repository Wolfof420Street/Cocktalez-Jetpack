package com.wolf.cocktalezandroid.ingredients.data.domain.network.dto


import Ingredient
import com.google.gson.annotations.SerializedName

data class IngredientsResponse(
    @SerializedName("drinks")
    val ingredients: List<Ingredient>
)