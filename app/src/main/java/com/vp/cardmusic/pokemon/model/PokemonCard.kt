package com.vp.cardmusic.pokemon.model

import com.vp.cardmusic.pokemon.data.dto.PokemonCardDto

data class PokemonCard(
    val id: String,
    val name: String,
    val setName: String,
    val cardNumber: String,
    val rarity: String,
    val cardType: String,
    val pokemonType: String,
    val artist: String,
    val imageUrl: String?,
    val marketPrice: Double?,
    val lowPrice: Double?,
    val activeListings: Int?,
    val verifiedSellers: Int?,
    val primaryPrinting: String,
    val lastUpdated: String?,
)

fun PokemonCardDto.toDomain(): PokemonCard? {
    val cardName = name?.takeIf { it.isNotBlank() } ?: return null
    return PokemonCard(
        id = tcgPlayerId ?: id ?: "$cardName-${cardNumber.orEmpty()}",
        name = cardName,
        setName = setName.orEmpty().ifBlank { "Unknown Set" },
        cardNumber = cardNumber.orEmpty().ifBlank { "—" },
        rarity = rarity.orEmpty().ifBlank { "Unknown" },
        cardType = cardType.orEmpty().ifBlank { "Pokémon" },
        pokemonType = pokemonType.orEmpty().ifBlank { "Unknown" },
        artist = artist.orEmpty().ifBlank { "Unknown" },
        imageUrl = imageCdnUrl800 ?: imageCdnUrl400 ?: imageCdnUrl ?: imageCdnUrl200,
        marketPrice = prices?.market,
        lowPrice = prices?.low,
        activeListings = prices?.listings,
        verifiedSellers = prices?.sellers,
        primaryPrinting = prices?.primaryPrinting.orEmpty().ifBlank { "—" },
        lastUpdated = prices?.lastUpdated,
    )
}
