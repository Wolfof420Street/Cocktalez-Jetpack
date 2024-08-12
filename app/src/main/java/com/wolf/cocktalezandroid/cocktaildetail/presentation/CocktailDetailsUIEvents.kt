package com.wolf.cocktalezandroid.cocktaildetail.presentation

import com.wolf.cocktalezandroid.favorites.local.Favorite

sealed interface CocktailDetailsUIEvents {

    data object NavigateBack : CocktailDetailsUIEvents

    data class AddToFavorites(val favorite: Favorite) :
        CocktailDetailsUIEvents

    data class RemoveFromFavorites(val favorite: Favorite) :
        CocktailDetailsUIEvents

}