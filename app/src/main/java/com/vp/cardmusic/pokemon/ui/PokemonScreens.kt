package com.vp.cardmusic.pokemon.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.vp.cardmusic.pokemon.model.PokemonCard
import com.vp.cardmusic.pokemon.viewmodel.PokemonUiState
import com.vp.cardmusic.pokemon.viewmodel.PokemonViewModel
import com.vp.cardmusic.ui.theme.AppColors
import java.util.Locale

@Composable
fun PokemonCardApp() {
    val viewModel: PokemonViewModel = viewModel(factory = PokemonViewModel.Factory)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedCard by remember { mutableStateOf<PokemonCard?>(null) }

    if (selectedCard == null) {
        PokemonHomeScreen(
            state = state,
            onQueryChange = viewModel::onQueryChange,
            onSearch = { viewModel.searchCards() },
            onSelectCategory = { viewModel.searchCards(it) },
            onCardClick = { selectedCard = it },
            onRetry = viewModel::retry,
        )
    } else {
        PokemonDetailScreen(
            card = selectedCard!!,
            onBack = { selectedCard = null },
        )
    }
}

@Composable
private fun PokemonHomeScreen(
    state: PokemonUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onSelectCategory: (String) -> Unit,
    onCardClick: (PokemonCard) -> Unit,
    onRetry: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(AppColors.Background),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { PokemonTopBar() }
        item {
            PokemonSearchBar(
                query = state.query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
            )
        }
        item { PokemonCategoryChips(query = state.query, onSelect = onSelectCategory) }
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Market Results (${state.cards.size})", color = AppColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text("USD • Near Mint", color = AppColors.TextSecondary, fontSize = 11.sp)
            }
        }
        when {
            state.isLoading -> item {
                Box(Modifier.fillMaxWidth().height(280.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        CircularProgressIndicator(color = AppColors.PokemonRed, strokeWidth = 2.dp, modifier = Modifier.size(30.dp))
                        Text("Fetching card prices...", color = AppColors.TextSecondary, fontSize = 13.sp)
                    }
                }
            }
            state.error != null -> item { PokemonErrorPanel(message = state.error, onRetry = onRetry) }
            state.cards.isEmpty() -> item {
                PokemonEmptyPanel(query = state.query, onRetry = onRetry)
            }
            else -> items(state.cards.chunked(2)) { rowCards ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    rowCards.forEach { card ->
                        Box(modifier = Modifier.weight(1f)) {
                            PokemonCardItem(card = card, onClick = { onCardClick(card) })
                        }
                    }
                    if (rowCards.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun PokemonErrorPanel(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 72.dp, bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = AppColors.ErrorRed, modifier = Modifier.size(36.dp))
        Text("Connection Failed", color = AppColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(message, color = AppColors.TextSecondary, fontSize = 12.sp)
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PokemonRed),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        ) {
            Text("↻  Try Again", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PokemonEmptyPanel(query: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 70.dp, bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = AppColors.TextMuted, modifier = Modifier.size(34.dp))
        Text("Card Not Found", color = AppColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text("No cards found for \"$query\".", color = AppColors.TextSecondary, fontSize = 12.sp)
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PokemonRed),
        ) { Text("Try Again", fontSize = 13.sp) }
    }
}

@Composable
private fun PokemonDetailScreen(card: PokemonCard, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(AppColors.Background)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(320.dp).background(
                        Brush.verticalGradient(listOf(Color(0xFF542A34), Color(0xFF20222D), AppColors.Background))
                    ),
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.align(Alignment.TopStart).padding(8.dp)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AppColors.TextPrimary)
                    }
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).padding(14.dp),
                        shape = RoundedCornerShape(50),
                        color = Color(0xFF1A1E28).copy(alpha = 0.85f),
                        onClick = onBack,
                    ) {
                        Text("×", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = AppColors.TextPrimary, fontSize = 20.sp)
                    }
                    if (card.imageUrl != null) {
                        AsyncImage(
                            model = card.imageUrl,
                            contentDescription = card.name,
                            modifier = Modifier.align(Alignment.Center).padding(top = 22.dp).height(230.dp),
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
            }
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("${card.name} - ${card.cardNumber}", color = AppColors.TextPrimary, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    Text(card.setName, color = AppColors.TextSecondary, fontSize = 12.sp)
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PricePanel(
                        modifier = Modifier.weight(1f),
                        label = "Market Price",
                        value = card.marketPrice?.let { formatUsd(it) } ?: "N/A",
                        accent = AppColors.PriceGreen,
                    )
                    PricePanel(
                        modifier = Modifier.weight(1f),
                        label = "Lowest Price",
                        value = card.lowPrice?.let { formatUsd(it) } ?: "N/A",
                        accent = AppColors.PriceYellow,
                    )
                }
            }
            item {
                Column(modifier = Modifier.padding(horizontal = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Market Activity", color = AppColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    DetailInfoRow("Active Listings", card.activeListings?.toString() ?: "—")
                    DetailInfoRow("Verified Sellers", card.verifiedSellers?.toString() ?: "—")
                    DetailInfoRow("Primary Printing", card.primaryPrinting)
                }
            }
            item {
                Column(modifier = Modifier.padding(horizontal = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Card Specifications", color = AppColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    DetailInfoRow("Card Number", card.cardNumber)
                    DetailInfoRow("Rarity", card.rarity)
                    DetailInfoRow("Type", card.pokemonType.ifBlank { card.cardType })
                    DetailInfoRow("Illustrator", card.artist)
                }
            }
        }
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp).height(48.dp),
            shape = RoundedCornerShape(9.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PokemonRed),
        ) {
            Text("Done", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PricePanel(modifier: Modifier, label: String, value: String, accent: Color) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = AppColors.Surface,
        border = BorderStroke(1.dp, AppColors.Border),
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(Modifier.size(8.dp).clip(RoundedCornerShape(2.dp)).background(accent))
                Text(label, color = AppColors.TextSecondary, fontSize = 11.sp)
            }
            Text(value, color = accent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun DetailInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)).background(AppColors.Surface).padding(horizontal = 12.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = AppColors.TextSecondary, fontSize = 12.sp)
        Spacer(Modifier.weight(1f))
        Text(value, color = AppColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

private fun formatUsd(value: Double): String = "$" + String.format(Locale.US, "%,.2f", value)
