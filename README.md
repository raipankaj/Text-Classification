# Text-Classification
A library to classify text with sentiment like positive or negative using MediaPipe Text Classification


```gradle
allprojects {
  repositories {
		...
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency
```gradle
dependencies {
  implementation 'com.github.raipankaj:Text-Classification:0.1.0'
  implementation 'com.google.mediapipe:tasks-text:latest.release'
}
```

To add text classification, first create the text classifier like below
```kotlin
val textClassifier = rememberTextClassifier(
  modelPath = "text_classification.tflite",
  classifierOptions = {
    setScoreThreshold(0.33f)
    setMaxResults(2)
  }
)
```
classifierOptions field is optional hence you can also create text classifier like below
```kotlin
val textClassifier = rememberTextClassifier(
  modelPath = "text_classification.tflite",
)
```

text_classification.tflite - Download the tflite model and keep that in the Assets folder, you may also store it online and download it on the fly based on request.
Eventually modelPath, just need a path for the TFLite model.

Finally, call detectSentiment on the textClassifier to get the result like below
```kotlin
textClassifier.detectSentiment(text) {
  result = it.classificationResult().classifications()
}
```

Download TFLite model from
https://storage.googleapis.com/download.tensorflow.org/models/tflite/text_classification/text_classification_v2.tflite


A simple demo
```kotlin
@Composable
fun Demo() {
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

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
```

