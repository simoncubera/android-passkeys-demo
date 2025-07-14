package ch.cubera.passkeysdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ch.cubera.passkeysdemo.ui.theme.PasskeysDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasskeysDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        MainScreen()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PasskeysDemoTheme {
        MainScreen()
    }
}