package bar.gtfo.intent_injector.intent

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.phone.SmsRetriever

class SmsConsentContract() : ActivityResultContract<Intent, String?>() {

    override fun createIntent(context: Context, input: Intent) = input

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)

}