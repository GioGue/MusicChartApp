package com.example.musicchartapp.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicchartapp.model.Track
import com.example.musicchartapp.ui.theme.MusicChartAppTheme
import com.example.musicchartapp.viewModel.TopTrackViewModel
import com.example.musicchartapp.viewModel.TopTracksUIState

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
    when (tracksViewModel.tracksUIState){
        is TopTracksUIState.Success ->
            TrackListScreen(tracksViewModel)
        is TopTracksUIState.Loading -> LoadingScreen()
        is TopTracksUIState.Error -> ErrorScreen()
    }
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
            //Text("Loading...")
            //CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
            LoadingScreen()

        }
    }
}


@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        BoxWithConstraints {
            val size = if (maxWidth < maxHeight) maxWidth else maxHeight
            val padding = (size / 4).coerceAtLeast(16.dp)
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(150.dp))
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    strokeWidth = 4.dp,
                    color = MaterialTheme.colors.primary
                )
                //Spacer(modifier = Modifier.height(padding))
                Spacer(modifier = Modifier.height(110.dp))
                Text(
                    text = "Loading...",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun ErrorScreen() {
    Text("Error retrieving tracks. TopChart selecter can not be used.")
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