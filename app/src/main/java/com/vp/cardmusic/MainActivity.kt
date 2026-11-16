package com.vp.cardmusic

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.vp.cardmusic.music.ui.MusicArtistApp
import com.vp.cardmusic.pokemon.ui.PokemonCardApp
import com.vp.cardmusic.ui.theme.AppColors
import com.vp.cardmusic.ui.theme.AssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val backgroundColor = Color.rgb(16, 19, 24)
        window.statusBarColor = backgroundColor
        window.navigationBarColor = backgroundColor
        window.decorView.setBackgroundColor(backgroundColor)
        setContent {
            AssignmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AppColors.Background,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                    ) {
                        // SOAL 1
                        // PokemonCardApp()

                        // SOAL 2
                        MusicArtistApp()
                    }
                }
            }
        }
    }
}
