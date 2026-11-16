package com.vp.cardmusic.music.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistSearchResponseDto(
    val artists: List<ArtistDto>? = null,
)

@Serializable
data class ArtistDto(
    val idArtist: String? = null,
    val strArtist: String? = null,
    val strGenre: String? = null,
    val strStyle: String? = null,
    val strCountry: String? = null,
    val intFormedYear: String? = null,
    val strArtistThumb: String? = null,
    val strArtistBanner: String? = null,
    val strArtistWideThumb: String? = null,
    val strArtistFanart: String? = null,
    val strBiography: String? = null,
    val strBiographyEN: String? = null,
)

@Serializable
data class AlbumSearchResponseDto(
    val album: List<AlbumDto>? = null,
)

@Serializable
data class AlbumDetailResponseDto(
    val album: List<AlbumDto>? = null,
)

@Serializable
data class AlbumDto(
    val idAlbum: String? = null,
    val strAlbum: String? = null,
    val strArtist: String? = null,
    val intYearReleased: String? = null,
    val strGenre: String? = null,
    val strStyle: String? = null,
    val strAlbumThumb: String? = null,
    val strDescription: String? = null,
    val strDescriptionEN: String? = null,
)

@Serializable
data class TrackResponseDto(
    val track: List<TrackDto>? = null,
)

@Serializable
data class TrackDto(
    val idTrack: String? = null,
    val strTrack: String? = null,
    val strArtist: String? = null,
    val strGenre: String? = null,
    val intDuration: String? = null,
    val intTrackNumber: String? = null,
)
