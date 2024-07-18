package com.wolf.cocktalezandroid.home.data.network.dto

import com.google.gson.annotations.SerializedName
import com.wolf.cocktalezandroid.home.domain.Category

data class CategoryResponse(
    @SerializedName("categories")
    val categories: List<Category>
)
