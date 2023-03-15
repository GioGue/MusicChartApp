package com.example.musicchartapp.viewModel

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.musicchartapp.model.TabItem

class ScreenViewModel: ViewModel(){
    var countryName: MutableState<String> = mutableStateOf("")

}

@Composable
fun MyBottomNavigation(items : List<TabItem>, navController: NavController){
    var selectedItem by remember { mutableStateOf(0) }
    BottomNavigation {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route)},
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.label) }
            )
        }
    }
}