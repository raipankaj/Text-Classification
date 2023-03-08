package com.pankaj.textclassification

import com.google.mediapipe.tasks.text.textclassifier.TextClassifier
import com.google.mediapipe.tasks.text.textclassifier.TextClassifierResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SentimentAnalyzer(
    private val textClassifier: TextClassifier,
    private val coroutineScope: CoroutineScope
) : SentimentAnalysis {

    override fun detectSentiment(
        input: String,
        onResult: (TextClassifierResult) -> Unit
    ) {
        coroutineScope.launch(Dispatchers.Default) {
            val result = textClassifier.classify(input)
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
}