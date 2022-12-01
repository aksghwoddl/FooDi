package com.lee.foodi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.foodi.data.room.entity.DiaryItem

import com.lee.foodi.databinding.DiaryFoodItemBinding

class DiaryFoodItemRecyclerAdapter : RecyclerView.Adapter<DiaryFoodItemRecyclerAdapter.DiaryFoodItemViewHolder>() {
    private var mDiaryList = mutableListOf<DiaryItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryFoodItemViewHolder {
        val binding = DiaryFoodItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return DiaryFoodItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryFoodItemViewHolder, position: Int) {
        holder.bind(mDiaryList[position])
    }

    override fun getItemCount() = mDiaryList.size

    fun setDiaryList(list : MutableList<DiaryItem>){
        mDiaryList = list
    }

    inner class DiaryFoodItemViewHolder(private val binding : DiaryFoodItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: DiaryItem){
            with(binding){
                timeTextView.text = data.time
                foodNameTextView.text = data.food?.foodName
                servingSizeTextView.text = data.servingSize
            }
        }
    }
}