package com.vp.cardmusic.music.service

import com.vp.cardmusic.music.data.dto.AlbumDetailResponseDto
import com.vp.cardmusic.music.data.dto.AlbumSearchResponseDto
import com.vp.cardmusic.music.data.dto.ArtistSearchResponseDto
import com.vp.cardmusic.music.data.dto.TrackResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheAudioDbService {
    @GET("{apiKey}/search.php")
    suspend fun searchArtist(
        @Path("apiKey") apiKey: String,
        @Query("s") artistName: String,
    ): ArtistSearchResponseDto

    @GET("{apiKey}/searchalbum.php")
    suspend fun searchAlbums(
        @Path("apiKey") apiKey: String,
        @Query("s") artistName: String,
    ): AlbumSearchResponseDto

    @GET("{apiKey}/album.php")
    suspend fun getAlbum(
        @Path("apiKey") apiKey: String,
        @Query("m") albumId: String,
    ): AlbumDetailResponseDto

    @GET("{apiKey}/track.php")
    suspend fun getAlbumTracks(
        @Path("apiKey") apiKey: String,
        @Query("m") albumId: String,
    ): TrackResponseDto
}
