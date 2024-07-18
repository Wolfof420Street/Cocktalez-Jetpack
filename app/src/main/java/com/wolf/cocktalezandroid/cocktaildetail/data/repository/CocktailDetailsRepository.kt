package com.wolf.cocktalezandroid.cocktaildetail.data.repository

import CocktailObject

import com.wolf.cocktalezandroid.common.data.network.CocktailDbApi
import com.wolf.cocktalezandroid.common.util.AppState

import timber.log.Timber
import javax.inject.Inject

class CocktailDetailsRepository @Inject constructor(private val api: CocktailDbApi) {
    // Cocktail Details
    suspend fun getCocktailDetails(cocktailId: String): AppState<CocktailObject> {
        val response = try {
            api.getCocktailDetails(cocktailId)
        } catch (e: Exception) {
            return AppState.Error("Unknown error occurred")
        }
        Timber.d("Cocktail details: $response")
        return AppState.Success(response.drinks.first())
    }

    suspend fun getRandomCocktail(): AppState<CocktailObject> {
        val response = try {
            api.getRandomCocktail()
        } catch (e: Exception) {
            return AppState.Error("Unknown error occurred")
        }
        Timber.d("Cocktail details: $response")
        return AppState.Success(response.drinks.first())
    }


}