package com.example.androidintensive_6_recyclerview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ContactAdapter(
    private var contactList: List<ContactInfo>,
    private val context: Context
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val contactClicked = context as ContactClicked
    private val contactToBundle = mutableListOf<ContactInfo>()

    inner class ContactViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.contact_name_text)
        private val lastName: TextView = view.findViewById(R.id.contact_lastName_text)
        private val phoneNumber: TextView = view.findViewById(R.id.contact_phoneNumber_text)
        private val image: ImageView = view.findViewById(R.id.contact_image_view)

        fun bind(contactInfo: ContactInfo) {
            name.text = contactInfo.name
            lastName.text = contactInfo.lastName
            phoneNumber.text = contactInfo.phoneNumber
            Glide.with(context).load(contactInfo.imageUrl).into(image)
        }
    }

    override fun getItemCount() = contactList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContactViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contact = contactList[position]
        (holder as ContactViewHolder).bind(contact)

        holder.itemView.setOnClickListener {
            contactList.forEach {
                contactToBundle.add(it)
            }
            val bundle = bundleOf(CONTACT_ADAPTER_TAG to contactToBundle)
            contactClicked.onContactClicked(position, bundle)
        }
    }

    interface ContactClicked {
        fun onContactClicked(position: Int, bundle: Bundle)
    }
}