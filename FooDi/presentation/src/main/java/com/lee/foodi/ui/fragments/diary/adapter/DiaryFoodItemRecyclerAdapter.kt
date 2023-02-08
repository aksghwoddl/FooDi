package com.lee.foodi.ui.fragments.diary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.lee.foodi.R
import com.lee.foodi.data.room.entity.DiaryItem
import com.lee.foodi.databinding.DiaryFoodItemBinding

class DiaryFoodItemRecyclerAdapter : RecyclerView.Adapter<DiaryFoodItemRecyclerAdapter.DiaryFoodItemViewHolder>() {
    private var mDiaryList = mutableListOf<DiaryItem>()
    private var mMenuItemClickListener : PopupMenu.OnMenuItemClickListener? = null
    private var mOnItemClickListener : OnItemClickListener? = null
    private lateinit var mSelectedDiaryItem : DiaryItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryFoodItemViewHolder {
        val binding = DiaryFoodItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return DiaryFoodItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryFoodItemViewHolder, position: Int) {
        holder.bind(mDiaryList[position])
    }

    override fun getItemCount() = mDiaryList.size

    fun getSelectedItem() = mSelectedDiaryItem

    fun setDiaryList(list : MutableList<DiaryItem>){
        mDiaryList = list
    }

    /**Set Popup Menu Click Listener **/
    fun setOnMenuItemClickListener(listener: PopupMenu.OnMenuItemClickListener){
        mMenuItemClickListener = listener
    }

    /**Set Item Click Listener **/
    interface OnItemClickListener{
        fun onItemClick(v: View, model : DiaryItem, position: Int) {}
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    inner class DiaryFoodItemViewHolder(private val binding : DiaryFoodItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: DiaryItem){
            with(binding){
                timeTextView.text = data.time
                foodNameTextView.text = data.food?.foodName
                servingSizeTextView.text = data.servingSize
            }
            val position = adapterPosition
            // Setting Listener
            if(position != RecyclerView.NO_POSITION){
                itemView.setOnLongClickListener {
                    mSelectedDiaryItem = data
                    val popupMenu = PopupMenu(binding.root.context , it)
                    popupMenu.menuInflater.inflate(R.menu.diary_selected_menu , popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener(mMenuItemClickListener!!)
                    popupMenu.show()
                    true
                }

                itemView.setOnClickListener {
                    mOnItemClickListener?.onItemClick(it , data , position)
                }
            }
        }
    }
}