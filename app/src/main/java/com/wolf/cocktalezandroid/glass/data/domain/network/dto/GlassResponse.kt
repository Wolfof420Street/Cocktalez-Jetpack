package com.wolf.cocktalezandroid.glass.data.domain.network.dto

import Glass
import com.google.gson.annotations.SerializedName

data class GlassResponse(
    @SerializedName("drinks")
    val categories: List<Glass>
)