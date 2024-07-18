package com.wolf.cocktalezandroid.home.data.network.dto

import Drinks
import com.google.gson.annotations.SerializedName

data class CocktailResponse(
    @SerializedName("drinks")
    val drinks: List<Drinks>
)




