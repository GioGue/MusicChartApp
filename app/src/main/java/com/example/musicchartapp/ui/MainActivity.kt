package com.example.musicchartapp.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Black
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
                    //TrackApp()
                    //NavigationExampleApp()
                    BottomNavigationApp()
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
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.secondary
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
fun NavigationVideo(){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                onNavigationIconClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }

                }
            )
        },
        drawerContent = {
            DrawerHeader()
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id = "home",
                        title = "Home",
                        contentDescription = "Go to home screen",
                        icon = Icons.Default.Home
                    ),
                    MenuItem(
                        id = "settings",
                        title = "Settings",
                        contentDescription = "Go to settings screen",
                        icon = Icons.Default.Settings
                    ),
                    MenuItem(
                        id = "help",
                        title = "Help",
                        contentDescription = "Go to help screen",
                        icon = Icons.Default.Info
                    ),
                ),
                onItemClick = {
                    println("Clicked on ${it.title}")
                }
            )
        }
    ) {

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
    var text by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text(text = "Type the name of the country:")
        OutlinedTextField(value = screenViewModel.countryName.value, onValueChange ={ screenViewModel.countryName.value= it} )
        Button(
            onClick = {navController.navigate("second/${screenViewModel.countryName.value}")},
            enabled = screenViewModel.countryName.value.isNotEmpty()
        ){
            Text("Search")
        }
    }


}
@Composable
fun SecondScreen(navController: NavController, parameter: String?){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text(text = "Top Chart for ")
        TrackApp(TopTrackViewModel(parameter!!))
        //Text(text = "Parameter from Home is $parameter")
        Button(
            onClick = {navController.navigateUp()}
        ){
            Text("Back to Home")
        }
    }

}

@Composable
fun InfoScreen() {
    Text("Info screen")
}

@Composable
fun BottomNavigationApp() {
    val items = listOf(
        TabItem("Home", Icons.Filled.Home, "Home"),
        //TabItem("Favourites", Icons.Filled.Favorite, "Favourites"),
        TabItem("Info", Icons.Filled.Info, "Info")
    )
    BasicLayout(items)
}

@Composable
fun BasicLayout(items: List<TabItem>) {
    val navController = rememberNavController()
    Scaffold (
        topBar = {TopAppBar{ Text("Top Music Chart")}},
        content = { MyNavController(navController = navController)},
        bottomBar = {MyBottomNavigation(items, navController)}
    )
}

@Composable
fun MyNavController(navController: NavHostController) {
    val navController2 = rememberNavController()
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