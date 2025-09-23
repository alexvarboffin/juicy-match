package com.nativegame

import android.content.Context
import android.webkit.WebView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

fun WebView.loadPrivacyPolicy(string: CharArray) {
    loadUrl(String(string))
}

fun WebView.loadPrivacyPolicy(string: String) {
    loadUrl(string)
}

fun openInCustomTab(context: Context, url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(context, url.toUri())
}
fun openInCustomTab(context: Context, url: CharArray) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    val x = String(url)
    //println("@@@@ $x")
    customTabsIntent.launchUrl(context, x.toUri())
}