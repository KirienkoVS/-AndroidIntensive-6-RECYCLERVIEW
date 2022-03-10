package com.example.androidintensive_6_recyclerview

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

internal fun readContactFromFile(context: Context): ArrayList<ContactInfo> {

    val contactsList = arrayListOf<ContactInfo>()

    try {
        val reader = BufferedReader(InputStreamReader(context.assets.open("contacts_list.txt")))
        val rawContactsList = reader.readLines()
        reader.close()

        val formattedContactList = mutableListOf<String>()
        rawContactsList.forEach { string ->
            formattedContactList.add(string.replace(" ", "-"))
        }

        formattedContactList.forEach { string ->
            val contact = string.split("|")
            contactsList.add(ContactInfo(
                name = contact[0],
                lastName = contact[1],
                phoneNumber = contact[2]
            ))
        }
    } catch (e: Exception) {
        Log.i("Utils", "${e.message}")
    }

    return contactsList
}
