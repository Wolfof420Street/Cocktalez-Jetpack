package com.wolf.cocktalezandroid.common.data.network


import com.wolf.cocktalezandroid.home.data.network.dto.CategoryResponse
import com.wolf.cocktalezandroid.home.data.network.dto.CocktailResponse
import com.wolf.cocktalezandroid.home.data.network.dto.FullCocktailResponse
import com.wolf.cocktalezandroid.glass.data.domain.network.dto.GlassResponse
import com.wolf.cocktalezandroid.ingredients.data.domain.network.dto.IngredientsResponse

import retrofit2.http.GET

import retrofit2.http.Query

interface CocktailDbApi {
    @GET("filter.php?a=Alcoholic")
    suspend fun getAlcoholicCocktails(): CocktailResponse

    @GET("popular.php")
    suspend fun getPopularCocktails(): FullCocktailResponse

    @GET("filter.php?a=Non_Alcoholic")
    suspend fun getNonAlcoholicCocktails(): CocktailResponse

    @GET("list.php?c=list")
    suspend fun getCategories(): CategoryResponse

    @GET("list.php?g=list")
    suspend fun getGlasses(): GlassResponse

    @GET("list.php?i=list")
    suspend fun getIngredients(): IngredientsResponse

    @GET("random.php")
    suspend fun getRandomCocktail(): FullCocktailResponse

    @GET("lookup.php")
    suspend fun getCocktailDetails(@Query("i") id: String): FullCocktailResponse

    @GET("filter.php")
    suspend fun getCocktailsByGlass(@Query("g") glass: String): CocktailResponse

    @GET("filter.php")
    suspend fun getCocktailsByCategory(@Query("c") category: String): CocktailResponse

    @GET("filter.php")
    suspend fun getCocktailsByIngredient(@Query("i") ingredient: String): CocktailResponse
}