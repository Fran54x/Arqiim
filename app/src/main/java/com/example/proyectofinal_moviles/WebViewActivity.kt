package com.example.proyectofinal_moviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.proyectofinal_moviles.R

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView = findViewById(R.id.wvNavegador)
        webView.loadUrl("https://www.colomos.ceti.mx/")

    }
}
