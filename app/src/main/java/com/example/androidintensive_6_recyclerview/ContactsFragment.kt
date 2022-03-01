package com.example.androidintensive_6_recyclerview

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.fragment.app.Fragment

class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private lateinit var contactClickListener: ContactClickListener
    private lateinit var contactsInfoToBundle: MutableList<List<ContactInfo>>
    private lateinit var contactsInfoFromBundle: MutableList<List<ContactInfo>>
    private lateinit var bundle: Bundle

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contactClickListener = context as ContactClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).title = "Contacts"

        contactsInfoToBundle = mutableListOf()
        contactsInfoFromBundle = mutableListOf()
        bundle = bundleOf(CONTACTS_FRAGMENT_TAG to contactsInfoToBundle)

        // Gets contacts info from bundle
        if (requireArguments().getBundle(NEW_CONTACT_INFO)?.get(CONTACT_DETAILS_FRAGMENT_TAG) != null) {
            contactsInfoFromBundle = requireArguments().getBundle(NEW_CONTACT_INFO)?.get(CONTACT_DETAILS_FRAGMENT_TAG) as MutableList<List<ContactInfo>>
        }

        // Loads contacts info to layout and sets click listener
        view.findViewById<LinearLayout>(R.id.contacts_list_layout).forEach { contactLayout ->
            contactLayout as ConstraintLayout

            if (contactsInfoFromBundle.isEmpty()) {
                getAllContactsInfoFromLayout(contactLayout)
            } else getAllContactsInfoFromBundle(contactLayout)

            onContactClickListener(contactLayout)
        }
    }

    private fun getAllContactsInfoFromLayout(contactLayout: ConstraintLayout) {

        fun getContactInfo(tag: String, contactLayout: ConstraintLayout): String {
            var contactProperty = ""
            contactLayout.forEach { textView ->
                textView as TextView
                if (textView.transitionName != null && textView.transitionName == tag) {
                    contactProperty = textView.text.toString()
                }
            }
            return contactProperty
        }

        val contactInfo = ContactInfo(
            name = getContactInfo(NAME, contactLayout),
            lastName = getContactInfo(LAST_NAME, contactLayout),
            phoneNumber = getContactInfo(PHONE_NUMBER, contactLayout),
            contactID = contactLayout.transitionName
        )

        contactsInfoToBundle.add(listOf(contactInfo))
    }

    private fun getAllContactsInfoFromBundle(contactLayout: ConstraintLayout) {

        val contactInfo = ContactInfo()

        contactsInfoFromBundle.forEach { contactObjectsList ->
            contactLayout.forEach { textView ->
                textView as TextView
                contactObjectsList.forEach { contactInfoObject ->
                    if (contactInfoObject.contactID == contactLayout.transitionName && textView.transitionName != null) {
                        when(textView.transitionName) {
                            NAME -> {
                                textView.text = contactInfoObject.name
                                contactInfo.name = textView.text.toString()
                            }
                            LAST_NAME -> {
                                textView.text = contactInfoObject.lastName
                                contactInfo.lastName = textView.text.toString()
                            }
                            PHONE_NUMBER -> {
                                textView.text = contactInfoObject.phoneNumber
                                contactInfo.phoneNumber = textView.text.toString()
                            }
                        }
                        contactInfo.contactID = contactLayout.transitionName
                    }
                }
            }
        }

        contactsInfoToBundle.add(listOf(contactInfo))
    }

    private fun onContactClickListener(contactLayout: ConstraintLayout) {
        contactLayout.setOnClickListener {
            contactClickListener.onContactClicked(bundle, contactLayout.transitionName)
        }
    }

    interface ContactClickListener {
        fun onContactClicked(bundle: Bundle, contactID: String)
    }

    companion object {
        private const val NEW_CONTACT_INFO = "NEW_CONTACT_INFO"
        fun newInstance(bundle: Bundle) = ContactsFragment().also {
            it.arguments = Bundle().apply {
                putBundle(NEW_CONTACT_INFO, bundle)
            }
        }
    }
}