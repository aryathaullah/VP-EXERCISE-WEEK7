package com.vp.cardmusic.music.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.vp.cardmusic.music.model.MusicAlbum
import com.vp.cardmusic.music.model.MusicArtist
import com.vp.cardmusic.music.model.MusicTrack
import com.vp.cardmusic.ui.theme.AppColors

@Composable
fun MusicTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier.size(34.dp).clip(CircleShape).background(AppColors.MusicGreen),
            contentAlignment = Alignment.Center,
        ) {
            Text("♫", color = AppColors.Background, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Text("Music Artist", color = AppColors.TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MusicSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(50),
        color = AppColors.SurfaceSoft,
        border = BorderStroke(1.dp, AppColors.Border.copy(alpha = 0.65f)),
    ) {
        Row(
            modifier = Modifier.padding(start = 14.dp, end = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            IconButton(onClick = onSearch, modifier = Modifier.size(30.dp)) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = AppColors.MusicGreen, modifier = Modifier.size(19.dp))
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = TextStyle(color = AppColors.TextPrimary, fontSize = 14.sp),
                cursorBrush = SolidColor(AppColors.MusicGreen),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) Text("Search artist...", color = AppColors.TextMuted, fontSize = 14.sp)
                        innerTextField()
                    }
                },
            )
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }, modifier = Modifier.size(34.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Clear search", tint = AppColors.TextSecondary, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun MusicArtistHero(artist: MusicArtist) {
    Box(
        modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp)).background(AppColors.Surface),
    ) {
        val imageUrl = artist.bannerUrl ?: artist.wideThumbUrl ?: artist.thumbUrl
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "${artist.name} banner",
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop,
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth().height(180.dp).background(
                Brush.verticalGradient(listOf(AppColors.Background.copy(alpha = 0.05f), AppColors.Background.copy(alpha = 0.18f), AppColors.Background.copy(alpha = 0.94f)))
            ),
        )
        Row(
            modifier = Modifier.align(Alignment.BottomStart).padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (artist.thumbUrl != null) {
                AsyncImage(
                    model = artist.thumbUrl,
                    contentDescription = "${artist.name} profile",
                    modifier = Modifier.size(42.dp).clip(CircleShape).background(AppColors.SurfaceSoft),
                    contentScale = ContentScale.Crop,
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(artist.name, color = AppColors.TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(artist.genre, color = AppColors.MusicGreen, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    if (artist.location.isNotBlank()) {
                        Text("•", color = AppColors.TextSecondary, fontSize = 11.sp)
                        Text(artist.location, color = AppColors.TextSecondary, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun MusicAlbumCard(album: MusicAlbum, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = AppColors.Surface,
        border = BorderStroke(1.dp, AppColors.Border.copy(alpha = 0.6f)),
    ) {
        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(7.dp)).background(ColorAlbumPlaceholder),
                contentAlignment = Alignment.Center,
            ) {
                if (album.coverUrl != null) {
                    AsyncImage(
                        model = album.coverUrl,
                        contentDescription = album.title,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Text("♪", color = AppColors.MusicGreen, fontSize = 36.sp)
                }
            }
            Text(album.title, color = AppColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(album.year, color = AppColors.MusicGreen, fontSize = 11.sp)
                Spacer(Modifier.weight(1f))
                Text(album.genre, color = AppColors.TextSecondary, fontSize = 10.sp, maxLines = 1)
            }
        }
    }
}

@Composable
fun MusicTrackRow(track: MusicTrack, index: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = (track.trackNumber ?: index + 1).toString().padStart(2, '0'),
            color = AppColors.MusicGreen,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(track.title, color = AppColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(track.genre.ifBlank { "Track" }, color = AppColors.TextSecondary, fontSize = 11.sp)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = AppColors.TextSecondary,
                modifier = Modifier.size(13.dp),
            )
            Text(formatTrackDuration(track.durationMs), color = AppColors.TextSecondary, fontSize = 11.sp)
        }
    }
}

private fun formatTrackDuration(durationMs: Long?): String {
    if (durationMs == null || durationMs <= 0L) return "—"
    val totalSeconds = durationMs / 1000L
    return "%02d:%02d".format(totalSeconds / 60L, totalSeconds % 60L)
}

private val ColorAlbumPlaceholder = Color(0xFF151B23)
