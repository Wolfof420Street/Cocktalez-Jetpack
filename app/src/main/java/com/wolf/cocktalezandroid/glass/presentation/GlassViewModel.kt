package com.wolf.cocktalezandroid.glass.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wolf.cocktalezandroid.common.domain.repository.PreferenceRepository
import com.wolf.cocktalezandroid.glass.domain.repository.GlassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlassViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val glassRepository: GlassRepository
) : ViewModel() {

    private val _glassUiState = MutableStateFlow(GlassScreenUiState())

    val glassUiState = _glassUiState.asStateFlow()

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
        getGlasses()
    }

    private fun getGlasses() {
        _glassUiState.update {
            it.copy(
                glasses = glassRepository.getGlasses()
            )
        }
    }

    fun refresh() {
        getGlasses()
    }




}