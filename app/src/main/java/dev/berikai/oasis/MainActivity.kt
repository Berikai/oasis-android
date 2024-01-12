package dev.berikai.oasis

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

const val oasis = "https://oasis.izmirekonomi.edu.tr"
var preferences: SharedPreferences? = null
var cookie = "null"

class MainActivity : AppCompatActivity() {
    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = getSharedPreferences("credentials", 0)

        webView = WebView(applicationContext)
        oasisAutoSignIn()
        setOasisWebView()
        setContentView(webView)
        serveOasis()
    }

    private fun oasisAutoSignIn() {
        val username = preferences!!.getString("username", "")!!
        val password = preferences!!.getString("password", "")!!
        val pin = preferences!!.getString("pin", "")!!

        if(username != "" && password != "" && pin != "") {
            cookie = getSessionCookie(oasis, username, password, pin)
            if(cookie.contains("advanced-frontend")) {
                preferences!!.edit().putString("last_cookie", cookie).apply()
                CookieManager.getInstance().setCookie(oasis, cookie)
            } else {
                CookieManager.getInstance().setCookie(oasis, preferences!!.getString("last_cookie", "")!!)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setOasisWebView() {
        webView!!.webViewClient = OasisWebViewClient()
        webView!!.settings.javaScriptEnabled = true

        webView!!.addJavascriptInterface(AndroidJSInterface, "Android")
    }

    private fun serveOasis() {
        val startPage = "$oasis/message/inbox" // Start at message page
        webView!!.loadUrl(startPage)
    }
}