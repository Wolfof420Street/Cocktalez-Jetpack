package com.wolf.cocktalezandroid.ingredientcocktails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wolf.cocktalezandroid.common.domain.repository.PreferenceRepository
import com.wolf.cocktalezandroid.ingredientcocktails.domain.repository.IngredientCocktailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientCocktailViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val ingredientCocktailRepository: IngredientCocktailRepository
) : ViewModel() {

    private val _ingredientCocktailUiState = MutableStateFlow(IngredientCocktailUiState())

    val ingredientCocktailUiState = _ingredientCocktailUiState.asStateFlow()

    val theme = preferenceRepository.getTheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0,
        )

    fun updateTheme(themeValue: Int) {
        viewModelScope.launch {
            preferenceRepository.saveTheme(themeValue)
        }
    }

    fun getCocktailsByIngredient(ingredient: String) {
        viewModelScope.launch {
            val result = ingredientCocktailRepository.getGlassesByIngredients(
                ingredient
            )

            _ingredientCocktailUiState.update {
                it.copy(
                    cocktails = result
                )
            }


        }
    }
}