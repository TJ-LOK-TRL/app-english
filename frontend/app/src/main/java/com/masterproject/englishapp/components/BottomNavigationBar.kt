package com.masterproject.englishapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import com.masterproject.englishapp.navigation.BottomNavItem
import com.masterproject.englishapp.navigation.NavigationActions
import com.masterproject.englishapp.navigation.Screen
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.utils.DummyNavigator

@Composable
fun BottomNavigationBar(
    navigator: NavigationActions,
    currentRoute: String?
) {
    // TODO: Improve this later
    val items = listOf(
        BottomNavItem.Practice,
        BottomNavItem.Videos,
        BottomNavItem.Statistics,
        BottomNavItem.Profile,
    )

    NavigationBar(
        modifier = Modifier.height(50.dp),
        containerColor = Color.White,
        windowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.screen.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navigator.navigate(item.screen) {
                        popUpTo(Screen.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = item.title,
                            modifier = Modifier.size(28.dp),
                            tint = if (isSelected) AppColors.Primary else Color(0xFF757575)
                        )

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(AppColors.Primary)
                            )
                        } else {
                            Spacer(modifier = Modifier.size(5.dp))
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppColors.Primary,
                    unselectedIconColor = Color(0xFF757575),
                    selectedTextColor = AppColors.Primary,
                    unselectedTextColor = Color(0xFF757575),
                    indicatorColor = Color.Transparent
                ),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BottomNavigationBarPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Text(
                    text = "Conteúdo da tela",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            BottomNavigationBar(
                navigator = DummyNavigator,
                currentRoute = BottomNavItem.Practice.screen.route
            )
        }
    }
}

