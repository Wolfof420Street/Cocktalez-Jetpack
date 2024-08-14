package com.wolf.cocktalezandroid.glassescocktails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wolf.cocktalezandroid.common.domain.repository.PreferenceRepository
import com.wolf.cocktalezandroid.glassescocktails.domain.repository.GlassCocktailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlassCocktailViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val cocktailGlassRepository: GlassCocktailRepository
) : ViewModel() {

    private val _glassCocktailUIState = MutableStateFlow(GlassCocktailUiState())

    val glassCocktailUIState = _glassCocktailUIState.asStateFlow()

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

    fun getCocktailsByGlass(glass: String) {
        viewModelScope.launch {
           val result = cocktailGlassRepository.getGlassesByCategory(
               glass
           )

            _glassCocktailUIState.update {
                it.copy(
                    cocktails = result
                )
            }


        }
    }
}