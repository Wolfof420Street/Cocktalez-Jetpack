package com.wolf.cocktalezandroid.ingredients.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wolf.cocktalezandroid.common.domain.repository.PreferenceRepository
import com.wolf.cocktalezandroid.common.util.Constants.INGREDIENT_BASE_URL
import com.wolf.cocktalezandroid.ingredients.domain.repository.IngredientsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientsViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val ingredientsRepository: IngredientsRepository
) : ViewModel() {

    private val _ingredientUiState = MutableStateFlow(IngredientsUIState())

    val ingredientsUIState = _ingredientUiState.asStateFlow()

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

    init {
        getIngredients()
    }

    fun onPageChanged(pageIndex : Int) {

        _ingredientUiState.update {
            it.copy(
                pageIndex = pageIndex
            )
        }
    }

    private fun getIngredients() {
        _ingredientUiState.update {
            it.copy(
               ingredients = ingredientsRepository.getIngredients()
            )
        }
    }

    fun refresh() {
        getIngredients()
    }




    fun generateIngredientImageUrl(ingredientName: String?): String {
        // Check if ingredientName is null and handle accordingly
        if (ingredientName.isNullOrBlank()) {
            // Handle null case, e.g., return a default image URL or throw an exception
            return "$INGREDIENT_BASE_URL${ingredientName}.png"
        }
        // Proceed with the actual URL generation logic
        return "$INGREDIENT_BASE_URL${ingredientName}.png"
    }


}