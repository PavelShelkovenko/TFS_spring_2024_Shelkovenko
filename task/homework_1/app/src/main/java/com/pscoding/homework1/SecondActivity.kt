package com.pscoding.homework1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pscoding.homework1.ui.theme.HomeWork1Theme

class SecondActivity: ComponentActivity() {

    private var contacts: ArrayList<String>? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action == GetContactsService.GET_CONTACTS_ACTION) {
                contacts = intent.getStringArrayListExtra(GetContactsService.CONTACTS_KEY)
                getContacts(contacts ?: arrayListOf())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWork()
        setContent {
            HomeWork1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }
    }

    private fun getContacts(contacts: ArrayList<String>) {
        val intent = Intent().apply {
            putStringArrayListExtra(GetContactsService.CONTACTS_KEY, contacts)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun setupWork() {
        val intentFilter = IntentFilter(GetContactsService.GET_CONTACTS_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                receiver,
                intentFilter,
                RECEIVER_NOT_EXPORTED
            )
        } else {
            registerReceiver(
                receiver,
                intentFilter
            )
        }
        GetContactsService.create(this).also {
            startService(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}