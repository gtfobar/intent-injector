package bar.gtfo.intent_injector

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PhishingActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phishing)

        intent.data?.let {
            contentResolver.openInputStream(it)
        }?.bufferedReader()?.run {
            readText()
        }?.also{
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }
}