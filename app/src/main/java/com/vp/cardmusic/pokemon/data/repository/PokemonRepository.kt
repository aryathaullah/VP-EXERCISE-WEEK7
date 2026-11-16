package com.vp.cardmusic.pokemon.data.repository

import com.vp.cardmusic.BuildConfig
import com.vp.cardmusic.core.network.safeApiCall
import com.vp.cardmusic.pokemon.model.PokemonCard
import com.vp.cardmusic.pokemon.model.toDomain
import com.vp.cardmusic.pokemon.service.PokemonService

class PokemonRepository(private val service: PokemonService) {
    companion object {
        const val RESULT_LIMIT = 20
    }

    suspend fun searchCards(query: String): Result<List<PokemonCard>> {
        if (BuildConfig.POKEMON_API_KEY.isBlank()) {
            return Result.failure(
                IllegalStateException("POKEMON_API_KEY belum tersedia di secrets.properties.")
            )
        }
        return safeApiCall {
            service.searchCards(query = query, limit = RESULT_LIMIT)
                .data
                .mapNotNull { it.toDomain() }
        }
    }
}
