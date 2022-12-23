package com.lee.foodi.ui.activities.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.foodi.data.rest.model.Food
import com.lee.foodi.databinding.SearchFoodItemBinding

class SearchFoodRecyclerAdapter : RecyclerView.Adapter<SearchFoodRecyclerAdapter.SearchFoodViewHolder>() {
    private var mSearchFoodList = mutableListOf<Food>()
    private var mItemClickListener : OnItemClickListener? = null

    interface OnItemClickListener{
        fun onItemClick(v: View, model : Food, position: Int) {}
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFoodViewHolder {
        val binding = SearchFoodItemBinding.inflate(LayoutInflater.from(parent.context) , parent  , false)
        return SearchFoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchFoodViewHolder, position: Int) {
        holder.bind(mSearchFoodList[position])
    }

    override fun getItemCount() = mSearchFoodList.size

    fun setList(foodList : MutableList<Food>){
        mSearchFoodList = foodList
    }

    inner class SearchFoodViewHolder(private val binding : SearchFoodItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(model : Food) {
            binding.foodNameTextView.text = model.foodName
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                itemView.setOnClickListener{
                    mItemClickListener?.onItemClick(it , model , position)
                }
            }
        }
    }
}