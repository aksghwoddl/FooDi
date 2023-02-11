package com.lee.foodi.ui.activities.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lee.domain.model.remote.Food
import com.lee.foodi.databinding.SearchFoodItemBinding

/**
 * 검색한 음식 목록을 관리하는 Adapter class
 * **/
class SearchFoodRecyclerAdapter : ListAdapter<Food , SearchFoodRecyclerAdapter.SearchFoodViewHolder>(DiffUtilCallBack()) {
    private var itemClickListener : OnItemClickListener? = null

    interface OnItemClickListener{
        fun onItemClick(v: View, model : Food , position: Int) {}
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFoodViewHolder {
        val binding = SearchFoodItemBinding.inflate(LayoutInflater.from(parent.context) , parent  , false)
        return SearchFoodViewHolder(binding , itemClickListener!!)
    }

    override fun onBindViewHolder(holder: SearchFoodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchFoodViewHolder(
        private val binding : SearchFoodItemBinding ,
        private val onItemClickListener : OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(model : Food) {
            binding.foodNameTextView.text = model.foodName
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                itemView.setOnClickListener{
                    onItemClickListener.onItemClick(it , model , position)
                }
            }
        }
    }

    private class DiffUtilCallBack : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }
    }
}