package com.wolf.cocktalezandroid.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.wolf.cocktalezandroid.home.data.repository.CocktailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class CocktailViewModel @Inject constructor(
    private val cocktailRepository: CocktailsRepository,
) : ViewModel() {
    private val _cocktailUiState = MutableStateFlow(CocktailUiState())
    val cocktailUiState = _cocktailUiState.asStateFlow()

    init {
        getAlcoholicCocktails()
        getNonAlcoholicCocktails()
        getPopularCocktails()

    }

    /**
     * Cocktails
     */
    fun getAlcoholicCocktails() {
        _cocktailUiState.update {
            it.copy(
                alcoholicCocktails = cocktailRepository.getAlcoholicCocktails().cachedIn(viewModelScope)
            )
        }
    }

    fun getNonAlcoholicCocktails() {
        _cocktailUiState.update {
            it.copy(
                nonAlcoholicCocktails = cocktailRepository.getNonAlcoholicCocktails().cachedIn(viewModelScope)
            )
        }
    }

    fun getPopularCocktails() {
        _cocktailUiState.update {
            it.copy(
                popularCocktails = cocktailRepository.getPopularCocktails().cachedIn(viewModelScope)
            )
        }
    }


    fun refreshAllData() {
        getAlcoholicCocktails()
        getNonAlcoholicCocktails()
        getPopularCocktails()

    }
}