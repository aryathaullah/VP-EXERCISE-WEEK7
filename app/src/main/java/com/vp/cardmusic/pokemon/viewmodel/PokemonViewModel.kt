package com.vp.cardmusic.pokemon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vp.cardmusic.core.network.ServiceLocator
import com.vp.cardmusic.core.network.toDisplayMessage
import com.vp.cardmusic.pokemon.data.repository.PokemonRepository
import com.vp.cardmusic.pokemon.model.PokemonCard
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PokemonUiState(
    val query: String = "Charizard",
    val cards: List<PokemonCard> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false,
)

class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        searchCards("Charizard")
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
    }

    fun searchCards(value: String = _uiState.value.query) {
        val query = value.trim()
        if (query.isBlank()) {
            _uiState.update { it.copy(error = "Masukkan nama kartu Pokemon terlebih dahulu.", cards = emptyList()) }
            return
        }
        searchJob?.cancel()
        _uiState.update {
            it.copy(query = query, isLoading = true, error = null, cards = emptyList(), hasSearched = true)
        }
        searchJob = viewModelScope.launch {
            repository.searchCards(query)
                .onSuccess { cards ->
                    _uiState.update { it.copy(cards = cards, isLoading = false, error = null) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.toDisplayMessage(), cards = emptyList()) }
                }
        }
    }

    fun retry() = searchCards()

    companion object {
        val Factory = viewModelFactory {
            initializer { PokemonViewModel(ServiceLocator.pokemonRepository) }
        }
    }
}
