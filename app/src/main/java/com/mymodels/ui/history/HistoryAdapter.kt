package com.mymodels.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.mymodels.R
import com.mymodels.data.ChatSession

class HistoryAdapter(

    private val sessions: MutableList<ChatSession>,
    private val onChatSelected: (ChatSession) -> Unit

) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.findViewById(R.id.chatTitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_history, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return sessions.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val session = sessions[position]

        holder.title.text = session.title

        // buka chat
        holder.itemView.setOnClickListener {

            onChatSelected(session)

        }

        // rename chat
        holder.itemView.setOnLongClickListener {

            val context = holder.itemView.context
            val editText = EditText(context)

            editText.setText(session.title)

            AlertDialog.Builder(context)
                .setTitle("Rename Chat")
                .setView(editText)
                .setPositiveButton("OK") { _, _ ->

                    session.title = editText.text.toString()

                    notifyItemChanged(position)

                }
                .setNegativeButton("Cancel", null)
                .show()

            true
        }
    }
}