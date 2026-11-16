package com.vp.cardmusic.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vp.cardmusic.core.network.ServiceLocator
import com.vp.cardmusic.core.network.toDisplayMessage
import com.vp.cardmusic.music.data.repository.MusicRepository
import com.vp.cardmusic.music.model.AlbumDetailBundle
import com.vp.cardmusic.music.model.MusicAlbum
import com.vp.cardmusic.music.model.MusicArtist
import com.vp.cardmusic.music.model.MusicTrack
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MusicUiState(
    val query: String = "Coldplay",
    val artist: MusicArtist? = null,
    val albums: List<MusicAlbum> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false,
)

data class AlbumDetailUiState(
    val selectedAlbum: MusicAlbum? = null,
    val album: MusicAlbum? = null,
    val tracks: List<MusicTrack> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

class MusicViewModel(private val repository: MusicRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MusicUiState())
    val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()

    private val _albumState = MutableStateFlow(AlbumDetailUiState())
    val albumState: StateFlow<AlbumDetailUiState> = _albumState.asStateFlow()

    private var searchJob: Job? = null
    private var albumJob: Job? = null

    init {
        searchArtist("Coldplay")
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
    }

    fun searchArtist(value: String = _uiState.value.query) {
        val query = value.trim()
        if (query.isBlank()) {
            _uiState.update { it.copy(error = "Masukkan nama artis terlebih dahulu.", artist = null, albums = emptyList()) }
            return
        }
        searchJob?.cancel()
        _uiState.update {
            it.copy(query = query, artist = null, albums = emptyList(), isLoading = true, error = null, hasSearched = true)
        }
        searchJob = viewModelScope.launch {
            try {
                coroutineScope {
                    val artistDeferred = async { repository.searchArtist(query).getOrThrow() }
                    val albumsDeferred = async { repository.searchAlbums(query).getOrThrow() }
                    val artist = artistDeferred.await()
                    val albums = albumsDeferred.await()
                    if (artist == null) {
                        _uiState.update {
                            it.copy(isLoading = false, artist = null, albums = emptyList(), error = null)
                        }
                    } else {
                        _uiState.update {
                            it.copy(isLoading = false, artist = artist, albums = albums, error = null)
                        }
                    }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                _uiState.update { it.copy(isLoading = false, error = error.toDisplayMessage()) }
            }
        }
    }

    fun retrySearch() = searchArtist()

    fun loadAlbum(album: MusicAlbum) {
        albumJob?.cancel()
        _albumState.value = AlbumDetailUiState(selectedAlbum = album, isLoading = true)
        albumJob = viewModelScope.launch {
            repository.getAlbumDetail(album.id)
                .onSuccess { bundle: AlbumDetailBundle ->
                    _albumState.value = AlbumDetailUiState(
                        selectedAlbum = album,
                        album = bundle.album,
                        tracks = bundle.tracks,
                    )
                }
                .onFailure { error ->
                    _albumState.value = AlbumDetailUiState(
                        selectedAlbum = album,
                        isLoading = false,
                        error = error.toDisplayMessage(),
                    )
                }
        }
    }

    fun retryAlbum() {
        _albumState.value.selectedAlbum?.let(::loadAlbum)
    }

    fun closeAlbum() {
        albumJob?.cancel()
        _albumState.value = AlbumDetailUiState()
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { MusicViewModel(ServiceLocator.musicRepository) }
        }
    }
}
