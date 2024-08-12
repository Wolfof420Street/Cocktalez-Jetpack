package com.wolf.cocktalezandroid.cocktaildetail.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wolf.cocktalezandroid.cocktaildetail.data.repository.CocktailDetailsRepository
import com.wolf.cocktalezandroid.common.util.AppState
import com.wolf.cocktalezandroid.favorites.domain.repository.FavoritesRepository
import com.wolf.cocktalezandroid.favorites.local.Favorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailDetailsViewModel @Inject constructor(
    private val repository: CocktailDetailsRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _cocktailDetailsUiState = MutableStateFlow(CocktailDetailsUiState())

    val cocktailDetailsUiState = _cocktailDetailsUiState.asStateFlow()

    fun getCocktailDetails(cocktailId: String) {
        viewModelScope.launch {
            _cocktailDetailsUiState.update { it.copy(isLoading = true) }
            when (val result = repository.getCocktailDetails(cocktailId)) {
                is AppState.Error -> {
                    _cocktailDetailsUiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is AppState.Success -> {
                    _cocktailDetailsUiState.update {
                        it.copy(
                            isLoading = false,
                            cocktailDetails = result.data
                        )
                    }
                }
                is AppState.Loading -> TODO()
            }
        }
    }

    fun isAFavorite(cocktailId: Int): Flow<Boolean> {
        return favoritesRepository.isFavorite(cocktailId)
    }

    fun insertFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoritesRepository.insertFavorite(favorite)
        }
    }

    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoritesRepository.deleteOneFavorite(favorite)
        }
    }
}