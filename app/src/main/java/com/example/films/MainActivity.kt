package com.example.films

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.films.ui.theme.FilmsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MoviePickerScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
private fun MoviePickerScreen(modifier: Modifier = Modifier) {
    val movies = stringArrayResource(R.array.movies)
    val localeKey = movies.contentHashCode()

    var remainingIndices by rememberSaveable(localeKey) {
        mutableStateOf(movies.indices.shuffled().toIntArray())
    }
    var selectedIndex by rememberSaveable(localeKey) { mutableStateOf<Int?>(null) }
    var isExhausted by rememberSaveable(localeKey) { mutableStateOf(false) }

    val displayedText = when {
        isExhausted -> stringResource(R.string.no_more_movies)
        selectedIndex != null -> movies[selectedIndex!!]
        else -> stringResource(R.string.pick_prompt)
    }

    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.subtitle),
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = displayedText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 40.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            modifier = Modifier.padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    if (remainingIndices.isEmpty()) {
                        selectedIndex = null
                        isExhausted = true
                    } else {
                        selectedIndex = remainingIndices.first()
                        remainingIndices = remainingIndices.drop(1).toIntArray()
                        isExhausted = false
                    }
                }
            ) {
                Text(stringResource(R.string.pick_movie))
            }
            OutlinedButton(
                onClick = {
                    remainingIndices = movies.indices.shuffled().toIntArray()
                    selectedIndex = null
                    isExhausted = false
                }
            ) {
                Text(stringResource(R.string.reset))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MoviePickerPreview() {
    FilmsTheme {
        MoviePickerScreen(modifier = Modifier.fillMaxSize())
    }
}
