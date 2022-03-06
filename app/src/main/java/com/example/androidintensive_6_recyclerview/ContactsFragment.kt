package com.example.androidintensive_6_recyclerview

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private var contactID: Int = -1
    private var isBundleEmpty: Boolean = false
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var contactListFromFile: List<ContactInfo>
    private lateinit var contactListFromBundle: List<ContactInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).title = "Contacts"

        contactID = requireArguments().getInt(CONTACT_ID)
        recyclerView = view.findViewById(R.id.recycler_view)
        contactListFromFile = readContactFromFile(requireContext())
        isBundleEmpty = requireArguments().getBundle(NEW_CONTACT_INFO)?.get(CONTACT_DETAILS_FRAGMENT_TAG) == null

        // Gets contact info from contactListFromBundle
        if (!isBundleEmpty) {
            contactListFromBundle = requireArguments().getBundle(NEW_CONTACT_INFO)?.get(CONTACT_DETAILS_FRAGMENT_TAG) as List<ContactInfo>
            contactAdapter = ContactAdapter(contactListFromBundle, requireContext())
            recyclerView.adapter = contactAdapter
        } else {
            contactAdapter = ContactAdapter(contactListFromFile, requireContext())
            recyclerView.adapter = contactAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search, menu)

        val item = menu.findItem(R.id.search_action)
        val searchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchList: ArrayList<ContactInfo> = arrayListOf()

                if (isBundleEmpty) {
                    contactListFromFile.forEach {
                        if (it.name!!.lowercase().contains(newText!!.lowercase())) {
                            searchList.add(it)
                        }
                    }
                } else {
                    contactListFromBundle.forEach {
                        if (it.name!!.lowercase().contains(newText!!.lowercase())) {
                            searchList.add(it)
                        }
                    }
                }

                if (searchList.isEmpty()) {
                    Toast.makeText(activity, "No Data Found..", Toast.LENGTH_SHORT).show()
                } else {
                    contactAdapter = ContactAdapter(searchList, requireContext())
                    recyclerView.adapter = contactAdapter
                }
                return false
            }
        })


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