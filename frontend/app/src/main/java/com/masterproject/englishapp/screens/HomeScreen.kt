package com.masterproject.englishapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.masterproject.englishapp.navigation.Screen

@Composable
fun HomeScreen(onNavigate: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = { onNavigate(Screen.RECORDER) }) {
            Text("Go to Recorder")
        }
        Button(onClick = { onNavigate(Screen.PRACTICE) }) {
            Text("Go to Practice")
        }
        Button(onClick = { onNavigate(Screen.PROFILE) }) {
            Text("Go to Profile")
        }
        Button(onClick = { onNavigate(Screen.CHAT) }) {
            Text("Go to Chat")
        }
        Button(onClick = { onNavigate(Screen.CAMERA) }) {
            Text("Go to Camera")
        }
    }
}