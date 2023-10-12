package com.asadullo.valyuta.Activity.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asadullo.valyuta.Activity.Models.MainValyuta
import com.asadullo.valyuta.databinding.ItemKursBinding

class KursAdapter(var list: ArrayList<MainValyuta> = ArrayList(), var click:Click) : RecyclerView.Adapter<KursAdapter.Vh>() {
    inner class Vh(var item: ItemKursBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(user: MainValyuta, position: Int) {
            item.titleKurs.text = list[position].title
            item.price.text = list[position].cb_price

            item.root.setOnClickListener {
                click.click(position, list)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemKursBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    interface Click{
        fun click(position: Int, list: List<MainValyuta>)
    }

}