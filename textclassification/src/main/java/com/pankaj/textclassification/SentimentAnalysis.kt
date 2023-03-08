package com.pankaj.textclassification

import android.app.LocaleConfig
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.text.textclassifier.TextClassifier
import com.google.mediapipe.tasks.text.textclassifier.TextClassifierResult
import kotlinx.coroutines.CoroutineScope
import java.nio.ByteBuffer

@Stable
interface SentimentAnalysis {
    fun detectSentiment(input: String, onResult: (TextClassifierResult) -> Unit)
}

@Composable
fun rememberTextClassifier(
    modelPath: String,
    delegate: Delegate = Delegate.CPU,
    classifierOptions: (TextClassifier.TextClassifierOptions.Builder.() -> TextClassifier.TextClassifierOptions.Builder)? = null
): SentimentAnalysis {
    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    return remember {
        val baseOptionsBuilder = BaseOptions.builder()
            .setDelegate(delegate)
            .setModelAssetPath(modelPath)

        val baseOptions = baseOptionsBuilder.build()
        setUpClassifier(localContext, coroutineScope, baseOptions, classifierOptions)
    }
}

@Composable
fun rememberTextClassifier(
    byteBuffer: ByteBuffer,
    delegate: Delegate = Delegate.CPU,
    classifierOptions: (TextClassifier.TextClassifierOptions.Builder.() -> TextClassifier.TextClassifierOptions.Builder)? = null
): SentimentAnalysis {
    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    return remember {
        val baseOptionsBuilder = BaseOptions.builder()
            .setDelegate(delegate)
            .setModelAssetBuffer(byteBuffer)

        val baseOptions = baseOptionsBuilder.build()
        setUpClassifier(localContext, coroutineScope, baseOptions, classifierOptions)
    }
}

private fun setUpClassifier(
    localContext: Context,
    coroutineScope: CoroutineScope,
    baseOptions: BaseOptions,
    classifierOptions: (TextClassifier.TextClassifierOptions.Builder.() -> TextClassifier.TextClassifierOptions.Builder)?
): SentimentAnalyzer {
    val optionsBuilder = if (classifierOptions == null) {
        TextClassifier.TextClassifierOptions.builder()
            .setBaseOptions(baseOptions)
    } else {
        TextClassifier.TextClassifierOptions.builder()
            .setBaseOptions(baseOptions).classifierOptions()
    }

    val options = optionsBuilder.build()
    return SentimentAnalyzer(
        TextClassifier.createFromOptions(localContext, options),
        coroutineScope
    )
}