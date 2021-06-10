package com.scorp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.scorp.data.Person


class NameAdapter(var list: List<Person>) : RecyclerView.Adapter<NameAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView? = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(
            android.R.layout.simple_list_item_1,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView?.text = "${list.elementAtOrNull(position)?.fullName ?: ""} (${list.elementAtOrNull(
            position
        )?.id ?: ""})"
    }

    override fun getItemCount() = list.size
}