package com.example.androidintensive_6_recyclerview

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private var isBundleEmpty: Boolean = false
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactListFromFile: ArrayList<ContactInfo>
    private lateinit var contactListFromBundle: ArrayList<ContactInfo>
    private val contactAdapter by lazy { ContactAdapter(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).title = "Contacts"

        recyclerView = view.findViewById(R.id.recycler_view)
        contactListFromFile = readContactFromFile(requireContext())
        isBundleEmpty = requireArguments().getBundle(NEW_CONTACT_INFO)?.get(CONTACT_DETAILS_FRAGMENT_TAG) == null

       // Sets RecyclerView decoration
        recyclerView.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            addItemDecoration(ContactItemDecoration(requireContext()))
        }

        // Gets contact info from file or bundle
        if (isBundleEmpty) {
            recyclerView.adapter = contactAdapter
            contactAdapter.setData(contactListFromFile)
        } else {
            contactListFromBundle = requireArguments().getBundle(NEW_CONTACT_INFO)?.get(CONTACT_DETAILS_FRAGMENT_TAG) as ArrayList<ContactInfo>
            recyclerView.adapter = contactAdapter
            contactAdapter.setData(contactListFromBundle)
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

                fun search(contactList: List<ContactInfo>) {
                    contactList.forEach { contact ->
                        if (contact.name!!.lowercase().contains(newText!!.lowercase())
                            || contact.lastName!!.lowercase().contains(newText.lowercase())) {
                            searchList.add(contact)
                        }
                    }
                }

                if (isBundleEmpty) {
                    search(contactListFromFile)
                } else {
                    search(contactListFromBundle)
                }

                if (searchList.isEmpty()) {
                    Toast.makeText(activity, "No data found...", Toast.LENGTH_SHORT).show()
                } else {
                    recyclerView.adapter = contactAdapter
                    contactAdapter.setData(searchList)
                }
                return false
            }
        })

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