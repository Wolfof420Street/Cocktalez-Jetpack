package com.wolf.cocktalezandroid.home.data.network.dto


import CocktailObject
import com.google.gson.annotations.SerializedName

data class FullCocktailResponse(
    @SerializedName("drinks")
    val drinks: List<CocktailObject>
)
