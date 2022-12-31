package io.culturebook.ui.theme.molecules

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import io.culturebook.ui.theme.AppIcons
import io.culturebook.ui.theme.AppIcons.getPainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewComposable(
    modifier: Modifier = Modifier,
    titleId: Int,
    url: String,
    onBack: () -> Unit
) {
    Scaffold(modifier, topBar = {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = { Text(stringResource(titleId)) },
            navigationIcon = {
                IconButton(onClick = { onBack() }) {
                    Icon(painter = AppIcons.arrow_back.getPainter(), contentDescription = "back")
                }
            })
    }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            AndroidView(
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        webViewClient = WebViewClient()
                        loadUrl(url)
                    }
                }, update = { it.loadUrl(url) })
        }

    }

}