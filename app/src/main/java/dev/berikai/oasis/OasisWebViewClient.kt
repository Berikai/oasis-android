package dev.berikai.oasis

import android.annotation.SuppressLint
import android.net.http.SslError
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient

object AndroidJSInterface {
    @JavascriptInterface
    fun sendCredential(key: String, value: String) {
        preferences!!.edit().putString(key, value).apply()
    }

    @JavascriptInterface
    fun logout() {
        CookieManager.getInstance().setCookie(oasis, "")
        with(preferences!!.edit()) {
            putString("username", "")
            putString("password", "")
            putString("pin", "")
            apply()
        }
    }
}

class OasisWebViewClient  : WebViewClient() {
    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(
        view: WebView?,
        handler: SslErrorHandler,
        error: SslError?
    ) {
        handler.proceed()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        if (url != null) {
            view?.loadUrl("javascript:(function(){\n" +
                    "    const login_button = document.getElementsByName(\"login-button\")[0];\n" +
                    "    const logout_button = document.getElementsByClassName(\"bg-danger dropdown-item\")[0];\n" +
                    "\n" +
                    "    if (login_button != null) {\n" +
                    "        login_button.addEventListener(\"click\", () => {\n" +
                    "\n" +
                    "            const username = document.getElementById(\"loginform-username\");\n" +
                    "            const password = document.getElementById(\"loginform-password\");\n" +
                    "            const pin = document.getElementById(\"input-help-hover\");\n" +
                    "        \n" +
                    "            if(username != null) {\n" +
                    "                Android.sendCredential(\"username\", username.value);\n" +
                    "            }\n" +
                    "        \n" +
                    "            if(password != null) {\n" +
                    "                 Android.sendCredential(\"password\", password.value);\n" +
                    "            }\n" +
                    "        \n" +
                    "            if(pin != null) {\n" +
                    "                Android.sendCredential(\"pin\", pin.value);\n" +
                    "            }\n" +
                    "        });\n" +
                    "    }\n" +
                    "\n" +
                    "    if (logout_button != null) {\n" +
                    "        logout_button.addEventListener(\"click\", () => {\n" +
                    "            Android.logout();\n" +
                    "        });\n" +
                    "    }\n" +
                    "})();")

            if (!url.contains("/login")) {
                val cookies = CookieManager.getInstance().getCookie(oasis).split(";")
                val it = cookies.iterator()
                while (it.hasNext()) {
                    val next = it.next()
                    if (next.contains("advanced-frontend")) {
                        preferences!!.edit().putString("last_cookie", next).apply()
                    }
                }
            }
        }
    }
}