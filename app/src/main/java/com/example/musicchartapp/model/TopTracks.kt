package com.example.musicchartapp.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class TopTracks (
    //val success: Boolean,
    val tracks: TracksWrapper
)

data class TracksWrapper(
    val track: List<Track>
)

data class Track(
    val name: String,
    val artist: Artist,
    var number: Int = 0
)

data class Artist(
    val name: String
)

const val BASE_URL = "https://ws.audioscrobbler.com"

interface LastFmApi {
    @GET("/2.0/?method=geo.getTopTracks&format=json")

    suspend fun getTopTracks(
        @Query("country") country: String,
        @Query("api_key") apy_key: String
    ): TopTracks

    companion object {
        var lastFmService: LastFmApi? = null

        fun getInstance(): LastFmApi {
            if (lastFmService === null) {
                lastFmService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(LastFmApi::class.java)
            }
            return lastFmService!!
        }
    }
}