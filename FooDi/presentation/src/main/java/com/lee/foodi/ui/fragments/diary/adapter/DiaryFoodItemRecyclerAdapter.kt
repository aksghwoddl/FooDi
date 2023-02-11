package com.lee.foodi.ui.fragments.diary.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lee.domain.model.local.DiaryItem
import com.lee.foodi.R
import com.lee.foodi.databinding.DiaryFoodItemBinding

/**
 * 다이어리의 음식 목록을 관리하는 Adapter class
 * **/
class DiaryFoodItemRecyclerAdapter : ListAdapter<DiaryItem , DiaryFoodItemRecyclerAdapter.DiaryFoodItemViewHolder>(DiffUtilCallBack()) {
    private var menuItemClickListener : PopupMenu.OnMenuItemClickListener? = null
    private var itemClickListener : OnItemClickListener? = null
    private lateinit var selectedDiaryItem : DiaryItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryFoodItemViewHolder {
        val binding = DiaryFoodItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return DiaryFoodItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryFoodItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getSelectedItem() = selectedDiaryItem

    /**
     * Popup Menu 클릭 리스너
     * **/
    fun setOnMenuItemClickListener(listener: PopupMenu.OnMenuItemClickListener){
        menuItemClickListener = listener
    }

    /**
     * Item 클릭 리스너
     * **/
    interface OnItemClickListener{
        fun onItemClick(v: View, model : DiaryItem, position: Int) {}
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

   inner class DiaryFoodItemViewHolder(
        private val binding : DiaryFoodItemBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: DiaryItem){
            with(binding){
                timeTextView.text = data.time
                foodNameTextView.text = data.food.foodName
                servingSizeTextView.text = data.servingSize
            }
            val position = adapterPosition
            selectedDiaryItem = data

            // Setting Listener
            if(position != RecyclerView.NO_POSITION){
                itemView.setOnLongClickListener {
                    val popupMenu = PopupMenu(binding.root.context , it)
                    popupMenu.menuInflater.inflate(R.menu.diary_selected_menu , popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener(menuItemClickListener)
                    popupMenu.show()
                    true
                }

                itemView.setOnClickListener {
                    itemClickListener?.onItemClick(it , data , position)
                }
            }
        }
    }

    private class DiffUtilCallBack : DiffUtil.ItemCallback<DiaryItem>() {
        override fun areItemsTheSame(oldItem: DiaryItem, newItem: DiaryItem): Boolean {
            return oldItem.index == newItem.index
        }

        override fun areContentsTheSame(oldItem: DiaryItem, newItem: DiaryItem): Boolean {
            return oldItem == newItem
        }
    }
}