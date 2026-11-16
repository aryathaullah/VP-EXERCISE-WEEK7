package com.vp.cardmusic.music.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.vp.cardmusic.music.model.MusicAlbum
import com.vp.cardmusic.music.viewmodel.AlbumDetailUiState
import com.vp.cardmusic.music.viewmodel.MusicUiState
import com.vp.cardmusic.music.viewmodel.MusicViewModel
import com.vp.cardmusic.ui.theme.AppColors

@Composable
fun MusicArtistApp() {
    val viewModel: MusicViewModel = viewModel(factory = MusicViewModel.Factory)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val albumState by viewModel.albumState.collectAsStateWithLifecycle()

    if (albumState.selectedAlbum != null) {
        MusicAlbumDetailScreen(
            state = albumState,
            onBack = viewModel::closeAlbum,
            onRetry = viewModel::retryAlbum,
        )
    } else {
        MusicArtistScreen(
            state = state,
            onQueryChange = viewModel::onQueryChange,
            onSearch = { viewModel.searchArtist() },
            onAlbumClick = viewModel::loadAlbum,
            onRetry = viewModel::retrySearch,
        )
    }
}

@Composable
private fun MusicArtistScreen(
    state: MusicUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onAlbumClick: (MusicAlbum) -> Unit,
    onRetry: () -> Unit,
) {
    val artist = state.artist
    var biographyExpanded by remember(artist?.id) { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().background(AppColors.Background),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) { MusicTopBar() }
        item(span = { GridItemSpan(maxLineSpan) }) {
            MusicSearchBar(query = state.query, onQueryChange = onQueryChange, onSearch = onSearch)
        }
        when {
            state.isLoading -> item(span = { GridItemSpan(maxLineSpan) }) {
                Box(Modifier.fillMaxWidth().height(390.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        CircularProgressIndicator(color = AppColors.MusicGreen, strokeWidth = 2.dp, modifier = Modifier.size(30.dp))
                        Text("Mencari artis dan album...", color = AppColors.TextSecondary, fontSize = 13.sp)
                    }
                }
            }
            state.error != null -> item(span = { GridItemSpan(maxLineSpan) }) {
                MusicErrorPanel(message = state.error, onRetry = onRetry)
            }
            artist == null -> item(span = { GridItemSpan(maxLineSpan) }) {
                MusicEmptyPanel(query = state.query, onRetry = onRetry)
            }
            else -> {
                item(span = { GridItemSpan(maxLineSpan) }) { MusicArtistHero(artist) }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Tentang Artis", color = AppColors.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        val biography = artist.biography.ifBlank { "Biografi artis belum tersedia." }
                        Text(
                            text = if (biographyExpanded || biography.length <= 420) biography else biography.take(420).substringBeforeLast(' ') + "...",
                            color = AppColors.TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                        )
                        if (biography.length > 420) {
                            Text(
                                text = if (biographyExpanded) "Tutup ringkasan" else "Baca selengkapnya",
                                color = AppColors.MusicGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 2.dp).clickable { biographyExpanded = !biographyExpanded },
                            )
                        }
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("Diskografi Album", color = AppColors.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("Koleksi rilisan resmi", color = AppColors.TextSecondary, fontSize = 10.sp)
                        }
                        Spacer(Modifier.weight(1f))
                        Surface(shape = RoundedCornerShape(50), color = AppColors.MusicGreen.copy(alpha = 0.14f)) {
                            Text(
                                "${state.albums.size} Album",
                                color = AppColors.MusicGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            )
                        }
                    }
                }
                if (state.albums.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text("Belum ada album yang tersedia.", color = AppColors.TextSecondary, fontSize = 12.sp)
                    }
                } else {
                    items(state.albums, key = { it.id }) { album ->
                        MusicAlbumCard(album = album, onClick = { onAlbumClick(album) })
                    }
                }
            }
        }
    }
}

@Composable
private fun MusicErrorPanel(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 100.dp, bottom = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = AppColors.ErrorRed, modifier = Modifier.size(36.dp))
        Text("Terjadi Kendala", color = AppColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(message, color = AppColors.TextSecondary, fontSize = 12.sp)
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.MusicGreen),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 9.dp),
        ) { Text("↻  Coba Lagi", color = AppColors.Background, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun MusicEmptyPanel(query: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 100.dp, bottom = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = AppColors.TextMuted, modifier = Modifier.size(36.dp))
        Text("Artis Tidak Ditemukan", color = AppColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text("Tidak ada hasil untuk \"$query\".", color = AppColors.TextSecondary, fontSize = 12.sp)
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.MusicGreen),
        ) { Text("Coba Lagi", color = AppColors.Background, fontSize = 13.sp) }
    }
}

@Composable
private fun MusicAlbumDetailScreen(
    state: AlbumDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    val album = state.album
    var descriptionExpanded by remember(state.selectedAlbum?.id) { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(AppColors.Background)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AppColors.TextPrimary)
            }
            Text("Detail Album", color = AppColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CircularProgressIndicator(color = AppColors.MusicGreen, strokeWidth = 2.dp, modifier = Modifier.size(30.dp))
                    Text("Memuat detail album & track list...", color = AppColors.TextSecondary, fontSize = 13.sp)
                }
            }
            state.error != null -> MusicErrorPanel(message = state.error, onRetry = onRetry)
            album != null -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            if (album.coverUrl != null) {
                                AsyncImage(
                                    model = album.coverUrl,
                                    contentDescription = album.title,
                                    modifier = Modifier.size(210.dp).clip(RoundedCornerShape(14.dp)),
                                    contentScale = ContentScale.Crop,
                                )
                            } else {
                                Box(
                                    modifier = Modifier.size(210.dp).clip(RoundedCornerShape(14.dp)).background(AppColors.Surface),
                                    contentAlignment = Alignment.Center,
                                ) { Text("♪", color = AppColors.MusicGreen, fontSize = 56.sp) }
                            }
                        }
                    }
                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(album.title, color = AppColors.TextPrimary, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                            Text(album.artist, color = AppColors.MusicGreen, fontSize = 13.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                MetaPill("Tahun ${album.year}")
                                MetaPill(album.genre)
                            }
                        }
                    }
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Deskripsi Album", color = AppColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            val description = album.description.ifBlank { "Deskripsi album belum tersedia." }
                            Text(
                                text = if (descriptionExpanded || description.length <= 360) description else description.take(360).substringBeforeLast(' ') + "...",
                                color = AppColors.TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                            )
                            if (description.length > 360) {
                                Text(
                                    if (descriptionExpanded) "Tutup ringkasan" else "Baca selengkapnya",
                                    color = AppColors.MusicGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.clickable { descriptionExpanded = !descriptionExpanded },
                                )
                            }
                        }
                    }
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("♫  Daftar Lagu", color = AppColors.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.weight(1f))
                            Surface(shape = RoundedCornerShape(50), color = AppColors.MusicGreen.copy(alpha = 0.14f)) {
                                Text("${state.tracks.size} Lagu", color = AppColors.MusicGreen, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                            }
                        }
                    }
                    if (state.tracks.isEmpty()) {
                        item { Text("Daftar lagu belum tersedia.", color = AppColors.TextSecondary, fontSize = 12.sp) }
                    } else {
                        itemsIndexed(state.tracks) { index, track ->
                            MusicTrackRow(track = track, index = index)
                            HorizontalDivider(color = AppColors.Border.copy(alpha = 0.6f), thickness = 0.6.dp)
                        }
                    }
                }
            }
            else -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Pilih album untuk melihat detail.", color = AppColors.TextSecondary, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun MetaPill(value: String) {
    Surface(shape = RoundedCornerShape(50), color = AppColors.Surface) {
        Text(value, color = AppColors.TextSecondary, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
    }
}
