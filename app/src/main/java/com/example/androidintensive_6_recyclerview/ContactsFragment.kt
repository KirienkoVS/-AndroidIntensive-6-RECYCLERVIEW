package com.example.androidintensive_6_recyclerview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private var contactID: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).title = "Contacts"

        contactID = requireArguments().getInt(CONTACT_ID)

        val contactListFromFile = readContactFromFile(requireContext())

        // Gets contact info from bundle
        val bundle = requireArguments().getBundle(NEW_CONTACT_INFO)?.get(CONTACT_DETAILS_FRAGMENT_TAG)

        if (bundle != null) {
            val contactFromBundle = requireArguments().getBundle(NEW_CONTACT_INFO)?.get(CONTACT_DETAILS_FRAGMENT_TAG) as List<ContactInfo>
            contactFromBundle.forEach { contact ->
                contactListFromFile[contactID].name = contact.name
                contactListFromFile[contactID].lastName = contact.lastName
                contactListFromFile[contactID].phoneNumber = contact.phoneNumber

            }
        }

        val adapter = ContactAdapter(contactListFromFile, requireContext())
        view.findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter
    }

    companion object {
        private const val CONTACT_ID = "CONTACT_ID"
        private const val NEW_CONTACT_INFO = "NEW_CONTACT_INFO"
        fun newInstance(position: Int, bundle: Bundle) = ContactsFragment().also {
            it.arguments = Bundle().apply {
                putInt(CONTACT_ID, position)
                putBundle(NEW_CONTACT_INFO, bundle)
            }
        }
    }
}