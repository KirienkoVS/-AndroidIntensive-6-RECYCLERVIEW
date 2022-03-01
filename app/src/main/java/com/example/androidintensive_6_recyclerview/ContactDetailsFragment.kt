package com.example.androidintensive_6_recyclerview

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class ContactDetailsFragment: Fragment(R.layout.fragment_contact_details) {

    private lateinit var bundle: Bundle
    private lateinit var contactID: String
    private lateinit var contactDetails: List<EditText>
    private lateinit var contactsInfoToBundle: MutableList<List<ContactInfo>>
    private lateinit var saveButtonClickListener: SaveButtonClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        saveButtonClickListener = context as SaveButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).title = "Contact details"

        contactsInfoToBundle = mutableListOf()
        contactID = requireArguments().getString(CONTACT_ID).toString()
        bundle = bundleOf(CONTACT_DETAILS_FRAGMENT_TAG to contactsInfoToBundle)

        contactDetails = listOf(
            view.findViewById(R.id.name_text),
            view.findViewById(R.id.last_name_text),
            view.findViewById(R.id.phone_number_text)
        )

        // Gets contact info from bundle
        requireArguments().getBundle(CONTACT_INFO)?.get(CONTACTS_FRAGMENT_TAG).also { contactsList ->
            contactsList as List<List<ContactInfo>>

            getContactsInfoFromBundle(contactsList)
            onSaveButtonClickListener(contactsList)
        }

        // Edit button
        view.findViewById<Button>(R.id.edit_button).also { editButton ->
            editButton.setOnClickListener {
                contactDetails.forEach { editText ->
                    val defaultBackground = EditText(requireContext()).background
                    editText.apply {
                        isFocusableInTouchMode = true
                        background = defaultBackground
                    }
                }
            }
        }
    }

    private fun prepareContactNewInfo(contactsList: List<List<ContactInfo>>) {
        contactsList.forEach { list ->
            list.forEach { contactInfoObject ->
                val contactInfo = ContactInfo()
                if (contactInfoObject.contactID == contactID) {
                    contactDetails.forEach { editText ->
                        when (editText.transitionName) {
                            NAME -> contactInfo.name = editText.text.toString()
                            LAST_NAME -> contactInfo.lastName = editText.text.toString()
                            PHONE_NUMBER -> contactInfo.phoneNumber = editText.text.toString()
                        }
                        contactInfo.contactID = contactID
                    }
                    contactsInfoToBundle.add(listOf(contactInfo))
                } else {
                    contactInfo.name = contactInfoObject.name
                    contactInfo.lastName = contactInfoObject.lastName
                    contactInfo.phoneNumber = contactInfoObject.phoneNumber
                    contactInfo.contactID = contactInfoObject.contactID
                    contactsInfoToBundle.add(listOf(contactInfo))
                }
            }
        }
    }

    private fun getContactsInfoFromBundle(contactsList: List<List<ContactInfo>>) {
        contactsList.forEach { list ->
            list.forEach { contactInfo ->
                if (contactInfo.contactID == contactID) {
                    contactDetails.forEach { editText ->
                        when (editText.transitionName) {
                            NAME -> editText.setText(contactInfo.name)
                            LAST_NAME -> editText.setText(contactInfo.lastName)
                            PHONE_NUMBER -> editText.setText(contactInfo.phoneNumber)
                        }
                    }
                }
            }
        }
    }

    private fun onSaveButtonClickListener(contactsList: List<List<ContactInfo>>) {
        requireView().findViewById<Button>(R.id.save_button).also { saveButton ->
            saveButton.setOnClickListener {
                contactDetails.forEach { editText ->
                    when (editText.transitionName) {
                        NAME -> {
                            editText.text = editText.text
                        }
                        LAST_NAME -> {
                            editText.text = editText.text
                        }
                        PHONE_NUMBER -> {
                            editText.text = editText.text
                        }
                    }
                }
                prepareContactNewInfo(contactsList)
                saveButtonClickListener.onSaveButtonClicked(bundle)
            }
        }
    }


    interface SaveButtonClickListener {
        fun onSaveButtonClicked(bundle: Bundle)
    }

    companion object {
        private const val CONTACT_ID = "CONTACT_ID"
        private const val CONTACT_INFO = "CONTACT_INFO"
        fun newInstance(bundle: Bundle, contactID: String) = ContactDetailsFragment().also {
            it.arguments = Bundle().apply {
                putBundle(CONTACT_INFO, bundle)
                putString(CONTACT_ID, contactID)
            }
        }
    }
}