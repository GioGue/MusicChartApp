package com.example.musicchartapp.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicchartapp.model.*
import com.example.musicchartapp.model.LastFmApi
import com.example.musicchartapp.model.Track
import kotlinx.coroutines.launch


sealed interface TopTracksUIState {
    object Success : TopTracksUIState
    object Error : TopTracksUIState
    object Loading : TopTracksUIState
}

class TopTrackViewModel(country: String): ViewModel() {

    var tracks by mutableStateOf<List<Track>>(emptyList())
        private set

    var tracksUIState by mutableStateOf<TopTracksUIState>(TopTracksUIState.Loading)
        private set

    init{
        getTracks(country)
    }

    fun getTracks(country: String) {
        viewModelScope.launch {
            var lastFmApi: LastFmApi? = null
            var n = 0

            try {
                lastFmApi = LastFmApi.getInstance()
                val topTracks = lastFmApi!!.getTopTracks(country)
                tracks = topTracks.tracks.track
                for (T in tracks) {
                    n = n + 1
                    T.number = n
                }
                    tracks = topTracks.tracks.track
                    tracksUIState = TopTracksUIState.Success
            } catch (e: Exception) {
                Log.e("*******", e.message.toString())
                tracks = emptyList()
                tracksUIState = TopTracksUIState.Error

            }
        }
    }
}