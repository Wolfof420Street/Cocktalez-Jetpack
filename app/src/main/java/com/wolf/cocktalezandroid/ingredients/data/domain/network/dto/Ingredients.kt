package com.wolf.cocktalezandroid.ingredients.data.domain.network.dto

import Drinks
import com.google.gson.annotations.SerializedName

data class IngredientsResponse(
    @SerializedName("drinks")
    val categories: List<Drinks>
)