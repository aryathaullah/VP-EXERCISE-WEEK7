package com.vp.cardmusic.music.model

import com.vp.cardmusic.music.data.dto.AlbumDto
import com.vp.cardmusic.music.data.dto.ArtistDto
import com.vp.cardmusic.music.data.dto.TrackDto

data class MusicArtist(
    val id: String,
    val name: String,
    val genre: String,
    val style: String,
    val location: String,
    val formedYear: String,
    val thumbUrl: String?,
    val bannerUrl: String?,
    val wideThumbUrl: String?,
    val biography: String,
)

data class MusicAlbum(
    val id: String,
    val title: String,
    val artist: String,
    val year: String,
    val genre: String,
    val coverUrl: String?,
    val description: String,
)

data class MusicTrack(
    val id: String,
    val title: String,
    val artist: String,
    val genre: String,
    val durationMs: Long?,
    val trackNumber: Int?,
)

data class AlbumDetailBundle(
    val album: MusicAlbum,
    val tracks: List<MusicTrack>,
)

fun ArtistDto.toDomain(): MusicArtist? {
    val artistName = strArtist?.takeIf { it.isNotBlank() } ?: return null
    val bioText = strBiographyEN?.takeIf { it.isNotBlank() }
        ?: strBiography?.takeIf { it.isNotBlank() }
        ?: "$artistName is a music artist in the ${strGenre.orEmpty()} genre from ${strCountry.orEmpty()}."

    return MusicArtist(
        id = idArtist.orEmpty(),
        name = artistName,
        genre = strGenre.orEmpty().ifBlank { "Unknown Genre" },
        style = strStyle.orEmpty(),
        location = strCountry.orEmpty(),
        formedYear = intFormedYear.orEmpty(),
        thumbUrl = strArtistThumb,
        bannerUrl = strArtistBanner,
        wideThumbUrl = strArtistWideThumb ?: strArtistFanart,
        biography = bioText,
    )
}

fun AlbumDto.toDomain(): MusicAlbum? {
    val albumName = strAlbum?.takeIf { it.isNotBlank() } ?: return null
    val descText = strDescriptionEN?.takeIf { it.isNotBlank() }
        ?: strDescription?.takeIf { it.isNotBlank() }
        ?: "$albumName is a music album by ${strArtist.orEmpty().ifBlank { "this artist" }}, released in ${intYearReleased.orEmpty()}."

    return MusicAlbum(
        id = idAlbum.orEmpty(),
        title = albumName,
        artist = strArtist.orEmpty(),
        year = intYearReleased.orEmpty().ifBlank { "—" },
        genre = strGenre.orEmpty().ifBlank { strStyle.orEmpty().ifBlank { "Unknown Genre" } },
        coverUrl = strAlbumThumb,
        description = descText,
    )
}

fun TrackDto.toDomain(index: Int): MusicTrack? {
    val trackName = strTrack?.takeIf { it.isNotBlank() } ?: return null
    return MusicTrack(
        id = idTrack ?: "$index-$trackName",
        title = trackName,
        artist = strArtist.orEmpty(),
        genre = strGenre.orEmpty(),
        durationMs = intDuration?.toLongOrNull(),
        trackNumber = intTrackNumber?.toIntOrNull(),
    )
}
