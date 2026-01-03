package com.masterproject.englishapp.screens.intro.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavOptionsBuilder
import com.masterproject.englishapp.R
import com.masterproject.englishapp.components.bubble.Bubble
import com.masterproject.englishapp.components.bubble.Side
import com.masterproject.englishapp.components.buttons.PrimaryButton
import com.masterproject.englishapp.components.buttons.SecondaryButton
import com.masterproject.englishapp.navigation.NavigationActions
import com.masterproject.englishapp.navigation.Screen
import com.masterproject.englishapp.ui.theme.AppColors
@Composable
fun WelcomeScreen(navigator: NavigationActions) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy((-20).dp)
            ) {
                Bubble("Hi there! I'm Jo!", Side.Bottom)

                Icon(
                    painter = painterResource(R.drawable.dragon_hello2),
                    contentDescription = null,
                    Modifier.size(344.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Firelingo",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.Primary
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                "Learn English whenever and wherever you want",
                fontSize = 20.sp,
                color = AppColors.Gray800,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryButton(
                "GET STARTED",
                onClick = { navigator.navigate(Screen.INTRO) },
                modifier = Modifier.fillMaxWidth()
            )

            SecondaryButton(
                "I ALREADY HAVE AN ACCOUNT",
                onClick = { navigator.navigate(Screen.LOGIN) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(
    name = "Xiaomi Redmi 9C",
    device = "spec:width=360dp,height=800dp,dpi=269",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFEEEEEE
)
@Composable
fun WelcomeScreenPreview() = WelcomeScreen(object : NavigationActions {
    override fun navigate(
        screen: Screen,
        params: String?,
        navOptions: NavOptionsBuilder.() -> Unit
    ) { }
})