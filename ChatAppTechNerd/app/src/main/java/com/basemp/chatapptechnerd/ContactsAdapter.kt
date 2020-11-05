package com.basemp.chatapptechnerd

import android.content.Intent
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.basemp.chatapptechnerd.entities.ContactEntity

class ContactsAdapter (val items: List<ContactEntity>) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TextView(parent.context))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = (position+1).toString() + ". "+ items[position].firstname + " " + items[position].lastname

        holder.textView.setOnClickListener{
            openActivity(holder.textView, items[position])
        }
    }
    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    private fun openActivity(view: TextView, item: ContactEntity) {
        val intent = Intent(view.getContext(), ContactRegisterActivity::class.java)
        intent.putExtra("id", item.id);
        intent.putExtra("firstname", item.firstname);
        intent.putExtra("lastname", item.lastname);
        intent.putExtra("email", item.email);
        intent.putExtra("phone", item.phone);
        view.getContext().startActivity(intent)
    }
}