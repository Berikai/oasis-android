package dev.berikai.oasis

import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

fun sendPostBody(outputStream: OutputStream, body: String) {
    val os: OutputStream = outputStream
    val osw = OutputStreamWriter(os, "UTF-8")
    osw.write(body)
    osw.flush()
    osw.close()
    os.close()
}

fun sendPost(address: String, body: String, cookie: String): HttpURLConnection {
    HttpsTrustManager.allowAllSSL()

    val url = URL(address)
    val openConnection = url.openConnection()

    with(openConnection as HttpURLConnection) {
        requestMethod = "POST"
        doOutput = true
        doInput= true
        useCaches = false
        connectTimeout = 1000 * 5
        instanceFollowRedirects = false
        HttpURLConnection.setFollowRedirects(false)

        setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        setRequestProperty("Cookie", cookie)
        sendPostBody(outputStream, body)
        connect()

        return openConnection
    }
}

fun getSessionCookie(oasisAddress: String, username: String, password: String, pin: String): String {
    var cookieReturn = "null"
    val thread = Thread {
        try {
            val address = "$oasisAddress/login"
            val pinResponse = sendPost(
                address,
                "LoginForm%5Busername%5D=$username&LoginForm%5Bpassword%5D=$password",
                "" // Must be empty at this stage
            )
            val pinCookie = pinResponse.headerFields["set-cookie"]?.iterator()!!.next()
            val response = sendPost(address, "LoginForm%5Bpin%5D=$pin", pinCookie)
            val cookie =  response.headerFields["set-cookie"]?.iterator()!!.next().split(";")[0]
            cookieReturn = cookie
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    thread.start()
    thread.join()

    return cookieReturn
}