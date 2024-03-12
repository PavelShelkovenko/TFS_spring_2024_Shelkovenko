package com.pscoding.homework1

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract

class GetContactsService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val contacts = getContacts()
        Intent(GET_CONTACTS_ACTION).apply {
            putStringArrayListExtra(CONTACTS_KEY, contacts)
            sendBroadcast(this)
        }
        stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }


    private fun getContacts(): ArrayList<String> {
        val contactsList = arrayListOf<String>()
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val columnIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                if (columnIndex >= 0) {
                    val contactName = it.getString(columnIndex)
                    contactsList.add(contactName)
                }
            }
        }
        return contactsList
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val CONTACTS_KEY = "contacts"
        const val GET_CONTACTS_ACTION = "get_contacts"

        fun create(context: Context): Intent {
            return Intent(context, GetContactsService::class.java)
        }
    }
}