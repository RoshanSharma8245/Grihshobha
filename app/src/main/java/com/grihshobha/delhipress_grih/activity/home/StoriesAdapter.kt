package com.grihshobha.delhipress_grih.activity.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grihshobha.delhipress_grih.R
import com.grihshobha.delhipress_grih.databinding.HomeItemBinding
import com.grihshobha.delhipress_grih.models.response.stories_response.Item


class StoriesAdapter(
    private val activity: TestActivityConcent,
    var itemsLists: List<Item?>?
) : RecyclerView.Adapter<StoriesAdapter.ItemHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ItemHolder {

        val itemBinding = HomeItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ItemHolder(itemBinding)
    }

    @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        val item: Item? = itemsLists?.get(position)
        holder.bind(item)

        holder.itemTv.setOnClickListener {


            activity.contentDetailFragment(item?.post_id)

        }


    }

    class ItemHolder(

        private val binding: HomeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(singleItem: Item?) {


            val text=" [ ID: ${singleItem?.post_id ?: "null"} ] ${singleItem?.title ?: "null"} "
            binding.itemTv.text = text

        }

        var itemTv: TextView = itemView.findViewById(R.id.itemTv)




    }

    override fun getItemCount(): Int {
        return itemsLists?.size ?: 0
    }

}




