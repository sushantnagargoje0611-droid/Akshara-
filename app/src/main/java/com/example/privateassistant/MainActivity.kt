package com.example.privateassistant
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isTtsReady by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AssistantDashboard(onSpeakClick = { text -> speakOut(text) })
                }
            }
        }
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isTtsReady = true
                speakOut("Hello, Private Assistant is active and ready.")
            }
        }
    }
    private fun speakOut(text: String) {
        if (isTtsReady) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }
    override fun onDestroy() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        super.onDestroy()
    }
}

@Composable
fun AssistantDashboard(onSpeakClick: (String) -> Unit) {
    var textToSpeak by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "🎙️ Assistant Active", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))
        OutlinedTextField(value = textToSpeak, onValueChange = { textToSpeak = it }, label = { Text("Assistant se kya bulwana hai?") }, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
        Button(onClick = { if (textToSpeak.isNotEmpty()) onSpeakClick(textToSpeak) }, modifier = Modifier.fillMaxWidth()) {
            Text("Assistant Ki Awaaz Suno")
        }
    }
}
