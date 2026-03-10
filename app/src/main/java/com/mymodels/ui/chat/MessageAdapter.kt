package com.mymodels.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mymodels.models.ChatMessage
import androidx.recyclerview.widget.RecyclerView
import com.mymodels.databinding.ItemAiMessageBinding
import com.mymodels.databinding.ItemUserMessageBinding

class MessageAdapter(
    private val messages: MutableList<ChatMessage>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val USER = 0
    private val AI = 1

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].role == "user") USER else AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == USER) {
            val binding = ItemUserMessageBinding.inflate(inflater, parent, false)
            UserHolder(binding)
        } else {
            val binding = ItemAiMessageBinding.inflate(inflater, parent, false)
            AIHolder(binding)
        }
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val msg = messages[position]

        if (holder is UserHolder) {
            holder.binding.textUser.text = msg.text
        }

        if (holder is AIHolder) {
            holder.binding.textAi.text = msg.text
        }
        fun addMessage(message: ChatMessage) {
           messages.add(message)
           notifyItemInserted(messages.size - 1)
          }
       }
    }

    class UserHolder(val binding: ItemUserMessageBinding)
        : RecyclerView.ViewHolder(binding.root)

    class AIHolder(val binding: ItemAiMessageBinding)
        : RecyclerView.ViewHolder(binding.root)
}