package bar.gtfo.intent_injector.intent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.ComponentActivity
import bar.gtfo.intent_injector.intent.IntentManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.Status

class IntentListener(private val intentManager: IntentManager) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        intentManager.onSmsReceived(intent)
    }

}