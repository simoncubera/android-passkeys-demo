package ch.cubera.passkeysdemo

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MainScreen(

) {
    var showWebView by remember { mutableStateOf(false) }

    if (showWebView) {
        WebViewScreen("https://keycloak.sc-loginsdk.cubera.ch/realms/scl/account/")
    } else {
        Button(onClick = { showWebView = true }) {
            Text("Login")
        }
    }
}
