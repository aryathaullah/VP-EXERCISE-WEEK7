package com.vp.cardmusic.pokemon.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.vp.cardmusic.pokemon.model.PokemonCard
import com.vp.cardmusic.ui.theme.AppColors
import java.util.Locale

@Composable
fun PokemonTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier.size(38.dp).clip(CircleShape).background(AppColors.PokemonRed),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(24.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension * 0.23f
                drawLine(Color.White, Offset(0f, center.y), Offset(size.width, center.y), strokeWidth = 2.dp.toPx())
                drawCircle(Color.White, radius = radius, center = center)
                drawCircle(AppColors.PokemonRed, radius = radius * 0.48f, center = center)
                drawCircle(Color.White, radius = radius, center = center, style = Stroke(width = 1.4.dp.toPx()))
            }
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text("PokéTracker", color = AppColors.TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Real-time TCG Card Valuation", color = AppColors.TextSecondary, fontSize = 11.sp)
        }
        Surface(shape = RoundedCornerShape(50), color = AppColors.MusicGreen.copy(alpha = 0.13f)) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Box(Modifier.size(6.dp).clip(CircleShape).background(AppColors.MusicGreen))
                Text("LIVE API", color = AppColors.MusicGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PokemonSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(13.dp),
        color = AppColors.SurfaceSoft,
        border = BorderStroke(1.dp, AppColors.Border.copy(alpha = 0.65f)),
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            IconButton(onClick = onSearch, modifier = Modifier.size(30.dp)) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = AppColors.PokemonRed, modifier = Modifier.size(19.dp))
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = TextStyle(color = AppColors.TextPrimary, fontSize = 14.sp),
                cursorBrush = SolidColor(AppColors.PokemonRed),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) Text("Search Pokémon card...", color = AppColors.TextMuted, fontSize = 14.sp)
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
fun PokemonCategoryChips(query: String, onSelect: (String) -> Unit) {
    val categories = listOf("Charizard", "Pikachu", "Mewtwo", "Gengar")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        categories.forEach { category ->
            val selected = query.equals(category, ignoreCase = true)
            FilterChip(
                selected = selected,
                onClick = { onSelect(category) },
                label = { Text(category, fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, if (selected) AppColors.PokemonRed else AppColors.Border),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = AppColors.Surface,
                    labelColor = AppColors.TextSecondary,
                    selectedContainerColor = AppColors.PokemonRed.copy(alpha = 0.16f),
                    selectedLabelColor = AppColors.TextPrimary,
                ),
            )
        }
    }
}

@Composable
fun PokemonCardItem(card: PokemonCard, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = AppColors.Surface,
        border = BorderStroke(1.dp, AppColors.Border.copy(alpha = 0.75f)),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(8.dp)).background(ColorCardImage),
                contentAlignment = Alignment.Center,
            ) {
                if (card.imageUrl != null) {
                    AsyncImage(
                        model = card.imageUrl,
                        contentDescription = card.name,
                        modifier = Modifier.fillMaxWidth().aspectRatio(0.72f).padding(2.dp),
                        contentScale = ContentScale.Fit,
                    )
                } else {
                    Text("POKÉMON", color = AppColors.TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(card.name, color = AppColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text("${card.setName} • ${card.cardNumber}", color = AppColors.TextSecondary, fontSize = 11.sp, maxLines = 1)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Surface(shape = RoundedCornerShape(6.dp), color = AppColors.MusicGreen.copy(alpha = 0.13f)) {
                    Text(
                        text = card.marketPrice?.let { "$" + String.format(Locale.US, "%.2f", it) } ?: "Price N/A",
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                        color = AppColors.PriceGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(Modifier.weight(1f))
                Text("Prices  ›", color = AppColors.TextSecondary, fontSize = 10.sp)
            }
        }
    }
}

private val ColorCardImage = Color(0xFF111720)
