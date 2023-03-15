package com.example.musicchartapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicchartapp.model.Track
import com.example.musicchartapp.ui.theme.MusicChartAppTheme
import com.example.musicchartapp.viewModel.TopTrackViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MusicChartAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TrackApp()
                }
            }
        }
    }
}

@Composable
fun TrackApp(tracksViewModel: TopTrackViewModel = viewModel()){
    TrackListScreen(tracksViewModel)
}


@Composable
fun TrackListScreen(tracksViewModel: TopTrackViewModel) {
    val tracks = tracksViewModel.tracks
    Scaffold(
        topBar = { TopAppBar(title = { Text("Top Tracks") }) }
    ) {
        if (tracks.isNotEmpty()) {
            TopTrackList(tracks,tracksViewModel)
        } else {
            CircularProgressIndicator(modifier = Modifier.fillMaxWidth()) //
        }
    }
}

@Composable
fun TopTrackList(tracks: List<Track>, tracksViewModel: TopTrackViewModel = viewModel()){
    LazyColumn {
        items(tracks) { track ->
            TrackListItem(track)
        }
    }
}



@Composable
fun TrackListItem(track: Track) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = track.number.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5
            )
            Text(
                text = track.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5
            )
            Text(
                text = track.artist.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6

            )
        }
    }
}





@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MusicChartAppTheme {
        Greeting("Android")
    }
}