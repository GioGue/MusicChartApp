package com.example.musicchartapp.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.musicchartapp.R
import com.example.musicchartapp.model.MenuItem
import com.example.musicchartapp.model.TabItem
import com.example.musicchartapp.model.Track
import com.example.musicchartapp.ui.theme.MusicChartAppTheme
import com.example.musicchartapp.viewModel.MyBottomNavigation
import com.example.musicchartapp.viewModel.ScreenViewModel
import com.example.musicchartapp.viewModel.TopTrackViewModel
import com.example.musicchartapp.viewModel.TopTracksUIState
import kotlinx.coroutines.launch

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
                    BottomNavigationApp()
                }
            }
        }
    }
}

@Composable
fun BottomNavigationApp() {
    val items = listOf(
        TabItem("Home", Icons.Filled.Home, "Home"),
        TabItem("Info", Icons.Filled.Info, "Info")
    )
    BasicLayout(items)
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
        // ${tracksViewModel.nameOfCountry}
        // "Top Tracks of ${tracksViewModel.nameOfCountry}"
        topBar = { TopAppBar(title = { Text(text = stringResource(R.string.Top_Bar_Title, tracksViewModel.nameOfCountry)) }) }
    ) {
        if (tracks.isNotEmpty()) {
            TopTrackList(tracks)
        } else {
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
                Spacer(modifier = Modifier.height(110.dp))
                Text(
                    text = stringResource(R.string.LoadingText),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun ErrorScreen() {
    Text(stringResource(R.string.ErrorScreenText))
}

@Composable
fun TopTrackList(tracks: List<Track>){
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
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.secondary
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = track.number.toString(),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                fontWeight = SemiBold
            )
            Text(
                text = track.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                fontWeight = SemiBold
            )
            Text(
                text = track.artist.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6

            )
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun NavigationApp(navController: NavController) {
    val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "home",
        ) {
            composable(route = "home") {
                HomeScreen(navController)
            }
            composable(route = "second/{parameter}",
                arguments = listOf(
                    navArgument("parameter") {
                        type = NavType.StringType

                    }
                )) {
                SecondScreen(
                    navController,
                    it.arguments?.getString("parameter")
                )
            }
        }
}


@Composable
fun HomeScreen(navController: NavController) {
    var screenViewModel: ScreenViewModel = viewModel()
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField( modifier = Modifier.fillMaxWidth() ,
            value = screenViewModel.countryName.value,
            onValueChange ={ screenViewModel.countryName.value= it},
            label = {Text(modifier = Modifier.fillMaxWidth(), text= stringResource(R.string.Type_Country_Name))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Button(
            onClick = {navController.navigate("second/${screenViewModel.countryName.value}")},
            enabled = screenViewModel.countryName.value.isNotEmpty()
        ){
            Text(stringResource(R.string.Search_button))
        }
    }


}
@Composable
fun SecondScreen(navController: NavController, parameter: String?){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        TrackApp(TopTrackViewModel(parameter!!))
        Button(
            onClick = {navController.navigateUp()}
        ){
            Text(stringResource(R.string.Back_home_button))
        }
    }

}

@Composable
fun InfoScreen() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        Text(stringResource(R.string.Info))
        Text(stringResource(R.string.Info_text))
        Text(stringResource(R.string.Author_app_name))
    }
}


@Composable
fun BasicLayout(items: List<TabItem>) {
    val navController = rememberNavController()
    Scaffold (
        topBar = {TopAppBar{ Text(stringResource(R.string.Title_Top_App_Bar))}},
        content = { MyNavController(navController = navController)},
        bottomBar = {MyBottomNavigation(items, navController)}
    )
}

@Composable
fun MyNavController(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "Home"

    ){

        composable(route= "Home") {
            NavigationApp(navController)
        }
        composable(route = "Info"){
            InfoScreen()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MusicChartAppTheme {
        BottomNavigationApp()
    }
}