package com.pankaj.sentimentanalysis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pankaj.sentimentanalysis.ui.theme.SentimentAnalysisTheme
import com.pankaj.textclassification.rememberTextClassifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SentimentAnalysisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var text by remember {
        mutableStateOf("")
    }
    var result by remember {
        mutableStateOf("")
    }

    val textClassifier = rememberTextClassifier(
        modelPath = "text_classification.tflite",
        classifierOptions = {
            setScoreThreshold(0.33f)
            setMaxResults(2)
        }
    )

    Column(Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = text, onValueChange = {
                text = it
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(onClick = {
            textClassifier.detectSentiment(text) {
                result = it.classificationResult().classifications().toString()
            }
        }) {
            Text(text = "Analyze")
        }

        Text(text = result)
    }
}
