package com.mymodels.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mymodels.R

data class Message(
    val text:String,
    val isUser:Boolean
)

class MessageAdapter:RecyclerView.Adapter<MessageAdapter.Holder>(){

    private val messages= mutableListOf<Message>()

    fun add(msg:Message){
        messages.add(msg)
        notifyItemInserted(messages.size-1)
    }

    class Holder(v:View):RecyclerView.ViewHolder(v){

        val text:TextView=v.findViewById(R.id.messageText)
    }

    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):Holder{

        val layout = if(viewType==0)
            R.layout.item_user_message
        else
            R.layout.item_ai_message

        val v=LayoutInflater.from(parent.context).inflate(layout,parent,false)

        return Holder(v)
    }

    override fun getItemViewType(position:Int):Int{

        return if(messages[position].isUser) 0 else 1
    }

    override fun getItemCount():Int=messages.size

    override fun onBindViewHolder(holder:Holder, position:Int){

        holder.text.text=messages[position].text
    }
}
