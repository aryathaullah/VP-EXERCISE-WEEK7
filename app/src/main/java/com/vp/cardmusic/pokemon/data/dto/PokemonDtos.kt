package com.vp.cardmusic.pokemon.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PokemonCardsResponseDto(
    val data: List<PokemonCardDto> = emptyList(),
    val metadata: PokemonMetadataDto? = null,
)

@Serializable
data class PokemonMetadataDto(
    val total: Int? = null,
    val count: Int? = null,
    val limit: Int? = null,
    val hasMore: Boolean? = null,
)

@Serializable
data class PokemonCardDto(
    val tcgPlayerId: String? = null,
    val id: String? = null,
    val name: String? = null,
    val setName: String? = null,
    val cardNumber: String? = null,
    val rarity: String? = null,
    val cardType: String? = null,
    val pokemonType: String? = null,
    val artist: String? = null,
    val imageCdnUrl: String? = null,
    val imageCdnUrl200: String? = null,
    val imageCdnUrl400: String? = null,
    val imageCdnUrl800: String? = null,
    val prices: PokemonPricesDto? = null,
)

@Serializable
data class PokemonPricesDto(
    val market: Double? = null,
    val low: Double? = null,
    val sellers: Int? = null,
    val listings: Int? = null,
    val primaryPrinting: String? = null,
    val lastUpdated: String? = null,
)
