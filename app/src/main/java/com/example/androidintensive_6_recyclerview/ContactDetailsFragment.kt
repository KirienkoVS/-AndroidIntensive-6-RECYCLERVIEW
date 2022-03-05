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

    private var contactID: Int = -1
    private lateinit var contactDetails: List<EditText>
    private lateinit var saveButtonClickListener: SaveButtonClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        saveButtonClickListener = context as SaveButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).title = "Contact details"

        contactID = requireArguments().getInt(CONTACT_ID)

        contactDetails = listOf(
            view.findViewById(R.id.contact_name_text),
            view.findViewById(R.id.last_name_text),
            view.findViewById(R.id.phone_number_text)
        )

        // Gets contact info from bundle
        requireArguments().getBundle(CONTACT_INFO)?.get(CONTACT_ADAPTER_TAG).also { contactList ->
            contactList as List<ContactInfo>

            getContactInfoFromBundle(contactList)
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

        // Save button
        requireView().findViewById<Button>(R.id.save_button).also { saveButton ->
            saveButton.setOnClickListener {
                val contactList = mutableListOf<ContactInfo>()
                val contact = ContactInfo()
                contactDetails.forEach { editText ->
                    when (editText.transitionName) {
                        NAME -> {
                            editText.text = editText.text
                            contact.name = editText.text.toString()
                        }
                        LAST_NAME -> {
                            editText.text = editText.text
                            contact.lastName = editText.text.toString()
                        }
                        PHONE_NUMBER -> {
                            editText.text = editText.text
                            contact.phoneNumber = editText.text.toString()
                        }
                    }
                }
                contactList.add(contact)
                val bundle = bundleOf(CONTACT_DETAILS_FRAGMENT_TAG to contactList)
                saveButtonClickListener.onSaveButtonClicked(contactID, bundle)
            }
        }
    }

    private fun getContactInfoFromBundle(contactList: List<ContactInfo>) {
        contactList.forEach { contact ->
            contactDetails.forEach { editText ->
                when (editText.transitionName) {
                    NAME -> editText.setText(contact.name)
                    LAST_NAME -> editText.setText(contact.lastName)
                    PHONE_NUMBER -> editText.setText(contact.phoneNumber)
                }
            }
        }
    }

    interface SaveButtonClickListener {
        fun onSaveButtonClicked(position: Int, bundle: Bundle)
    }

    companion object {
        private const val CONTACT_ID = "CONTACT_ID"
        private const val CONTACT_INFO = "CONTACT_INFO"
        fun newInstance(position: Int, bundle: Bundle) = ContactDetailsFragment().also {
            it.arguments = Bundle().apply {
                putInt(CONTACT_ID, position)
                putBundle(CONTACT_INFO, bundle)
            }
        }
    }
}