package ch.cubera.passkeysdemo

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import kotlinx.coroutines.CoroutineScope

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(url: String) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val credentialManagerHandler = activity?.let { CredentialManagerHandler(it) }
    val coroutineScope = rememberCoroutineScope()

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.safeBrowsingEnabled = false
                settings.setGeolocationEnabled(true)
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                val listenerSupported = WebViewFeature.isFeatureSupported(
                    WebViewFeature.WEB_MESSAGE_LISTENER
                )
                if (listenerSupported && activity != null && credentialManagerHandler != null) {
                    // Inject local JavaScript that calls Credential Manager.
                    hookWebAuthnWithListener(
                        this,
                        activity,
                        coroutineScope,
                        credentialManagerHandler
                    )
                } else {
                    // Fallback routine for unsupported API levels.
                }

                loadUrl(url)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

/**
 * Connects the local app logic with the web page via injection of javascript through a
 * WebListener. Handles ensuring the [PasskeyWebListener] is hooked up to the webView page
 * if compatible.
 */
fun hookWebAuthnWithListener(
    webView: WebView,
    activity: Activity,
    coroutineScope: CoroutineScope,
    credentialManagerHandler: CredentialManagerHandler
) {
    // [START android_identity_create_webview_object]
    val passkeyWebListener = PasskeyWebListener(activity, coroutineScope, credentialManagerHandler)

    val webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            webView.evaluateJavascript(PasskeyWebListener.INJECTED_VAL, null)
        }
    }

    webView.webViewClient = webViewClient
    // [END android_identity_create_webview_object]

    // [START android_identity_set_web]
    val rules = setOf("*")
    if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
        WebViewCompat.addWebMessageListener(webView, PasskeyWebListener.INTERFACE_NAME,
            rules, passkeyWebListener)
    }
    // [END android_identity_set_web]
}