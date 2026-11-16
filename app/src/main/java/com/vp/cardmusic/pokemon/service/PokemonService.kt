package com.vp.cardmusic.pokemon.service

import com.vp.cardmusic.pokemon.data.dto.PokemonCardsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonService {
    @GET("cards")
    suspend fun searchCards(
        @Query("search") query: String,
        @Query("limit") limit: Int = 20,
    ): PokemonCardsResponseDto
}
