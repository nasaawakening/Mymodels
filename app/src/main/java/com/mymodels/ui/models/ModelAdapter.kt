package com.mymodels.ui.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mymodels.R

class ModelAdapter(
    val click:(String)->Unit
):RecyclerView.Adapter<ModelAdapter.ViewHolder>() {

    private val models = mutableListOf<String>()

    fun submit(list:List<String>){
        models.clear()
        models.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        val name:TextView = v.findViewById(R.id.modelName)
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):ViewHolder{

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_model,parent,false)

        return ViewHolder(v)
    }

    override fun getItemCount():Int = models.size

    override fun onBindViewHolder(holder:ViewHolder, position:Int){

        val model=models[position]

        holder.name.text=model

        holder.itemView.setOnClickListener{
            click(model)
        }
    }
}
