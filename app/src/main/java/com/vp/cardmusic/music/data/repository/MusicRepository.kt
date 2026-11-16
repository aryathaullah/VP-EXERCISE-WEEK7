package com.vp.cardmusic.music.data.repository

import com.vp.cardmusic.BuildConfig
import com.vp.cardmusic.core.network.safeApiCall
import com.vp.cardmusic.music.model.AlbumDetailBundle
import com.vp.cardmusic.music.model.MusicAlbum
import com.vp.cardmusic.music.model.MusicArtist
import com.vp.cardmusic.music.model.MusicTrack
import com.vp.cardmusic.music.model.toDomain
import com.vp.cardmusic.music.service.TheAudioDbService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MusicRepository(private val service: TheAudioDbService) {
    private val apiKey: String get() = BuildConfig.THEAUDIODB_API_KEY

    private fun validateKey(): String {
        check(apiKey.isNotBlank()) { "THEAUDIODB_API_KEY belum tersedia." }
        return apiKey
    }

    suspend fun searchArtist(name: String): Result<MusicArtist?> = safeApiCall {
        service.searchArtist(validateKey(), name).artists?.firstNotNullOfOrNull { it.toDomain() }
    }

    suspend fun searchAlbums(artistName: String): Result<List<MusicAlbum>> = safeApiCall {
        service.searchAlbums(validateKey(), artistName).album.orEmpty().mapNotNull { it.toDomain() }
    }

    suspend fun getAlbumDetail(albumId: String): Result<AlbumDetailBundle> = safeApiCall {
        coroutineScope {
            val albumDeferred = async {
                service.getAlbum(validateKey(), albumId).album?.firstNotNullOfOrNull { it.toDomain() }
            }
            val tracksDeferred = async {
                service.getAlbumTracks(validateKey(), albumId).track.orEmpty()
                    .mapIndexedNotNull { index, track -> track.toDomain(index + 1) }
            }
            val album = albumDeferred.await()
                ?: throw IllegalStateException("Detail album tidak ditemukan.")
            val tracks = tracksDeferred.await()
            AlbumDetailBundle(album = album, tracks = tracks)
        }
    }
}
