package com.example.androidintensive_6_recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ContactAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var contactList = arrayListOf<ContactInfo>()
    private val contactToBundle = arrayListOf<ContactInfo>()
    private val contactClicked = context as ContactClicked

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

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contact = contactList[position]
        contact.contactID = position
        (holder as ContactViewHolder).bind(contact)

        holder.itemView.setOnClickListener {
            contactList.forEach {
                contactToBundle.add(it)
            }
            val bundle = bundleOf(CONTACT_ADAPTER_TAG to contactToBundle)
            contactClicked.onContactClicked(position, bundle)
        }

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context).run {
                setTitle("Delete contact?")
                setMessage("This contact will be deleted!")
                setIcon(R.drawable.icon_warning)
                setPositiveButton("Delete") { _, _ ->
                    contactList.remove(contact)
                    notifyDataSetChanged()
                }
                setNegativeButton("Cancel") { _, _ -> }
                create()
                show()
            }
            return@setOnLongClickListener true
        }
    }

    fun setData(newContactList: ArrayList<ContactInfo>) {
        val diffUtil = ContactDiffUtil(contactList, newContactList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        contactList = newContactList
        diffResult.dispatchUpdatesTo(this)
    }

    interface ContactClicked {
        fun onContactClicked(position: Int, bundle: Bundle)
    }
}

class ContactDiffUtil(
    private val oldList: List<ContactInfo>,
    private val newList: List<ContactInfo>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return  oldList[oldItemPosition].contactID == newList[newItemPosition].contactID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

class ContactItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val marginTop = context.resources.getDimensionPixelOffset(R.dimen.margin_top)
    private val marginBottom = context.resources.getDimensionPixelOffset(R.dimen.margin_bottom)
    private val marginLeft = context.resources.getDimensionPixelOffset(R.dimen.margin_left)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = marginTop
        outRect.bottom = marginBottom
        outRect.left = marginLeft
    }
}