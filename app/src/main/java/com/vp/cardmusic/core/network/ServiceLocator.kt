package com.vp.cardmusic.core.network

import com.vp.cardmusic.BuildConfig
import com.vp.cardmusic.music.data.repository.MusicRepository
import com.vp.cardmusic.music.service.TheAudioDbService
import com.vp.cardmusic.pokemon.data.repository.PokemonRepository
import com.vp.cardmusic.pokemon.service.PokemonService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ServiceLocator {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val converterFactory by lazy {
        json.asConverterFactory("application/json".toMediaType())
    }

    private val pokemonClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().apply {
                    if (BuildConfig.POKEMON_API_KEY.isNotBlank()) {
                        header("Authorization", "Bearer ${BuildConfig.POKEMON_API_KEY}")
                    }
                }.build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
                else HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }

    private val audioClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val pokemonService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.pokemonpricetracker.com/api/v2/")
            .client(pokemonClient)
            .addConverterFactory(converterFactory)
            .build()
            .create(PokemonService::class.java)
    }

    private val theAudioDbService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.theaudiodb.com/api/v1/json/")
            .client(audioClient)
            .addConverterFactory(converterFactory)
            .build()
            .create(TheAudioDbService::class.java)
    }

    val pokemonRepository: PokemonRepository by lazy { PokemonRepository(pokemonService) }
    val musicRepository: MusicRepository by lazy { MusicRepository(theAudioDbService) }
}
