package com.pscoding.homework1

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pscoding.homework1.ui.theme.HomeWork1Theme

class MainActivity : ComponentActivity() {

    private val contacts: MutableState<List<String>> = mutableStateOf(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            contacts.value = savedInstanceState.getStringArrayList(CONTACTS_KEY) ?: emptyList()
        }

        val getContactsContract = object : ActivityResultContract<Intent, ArrayList<String>?>() {
            override fun createIntent(context: Context, input: Intent): Intent {
                return input
            }

            override fun parseResult(resultCode: Int, intent: Intent?): ArrayList<String>? {
                if (resultCode == RESULT_OK) {
                    return intent?.getStringArrayListExtra(GetContactsService.CONTACTS_KEY)
                        ?: arrayListOf()
                }
                return null
            }
        }

        setContent {
            HomeWork1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val permissionGranted = remember {
                        mutableStateOf(false)
                    }

                    val getContactsLauncher = rememberLauncherForActivityResult(
                        contract = getContactsContract
                    ) {
                        contacts.value = it ?: emptyList()
                    }

                    val permissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            permissionGranted.value = isGranted
                        }
                    )

                    LaunchedEffect(key1 = true) {
                        val result = checkSelfPermission(
                            Manifest.permission.READ_CONTACTS
                        )
                        if (result == 0) {
                            permissionGranted.value = true
                        } else {
                            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            modifier = Modifier.padding(16.dp),
                            onClick = {
                                if (permissionGranted.value) {
                                    val intent = Intent(
                                        context,
                                        SecondActivity::class.java
                                    )
                                    getContactsLauncher.launch(intent)
                                } else {
                                    permissionLauncher.launch(
                                        Manifest.permission.READ_CONTACTS
                                    )
                                }
                            }) {
                            Text(text = "Get contacts")
                        }
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LazyColumn(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                items(contacts.value) {
                                    Text(text = it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(CONTACTS_KEY, contacts.value as java.util.ArrayList<String>)
    }

    companion object {
        private const val CONTACTS_KEY = "contacts_key"
    }
}