package com.lee.foodi.ui.activities.add

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lee.foodi.R
import com.lee.foodi.common.FoodiNewApplication
import com.lee.foodi.common.NOT_AVAILABLE
import com.lee.foodi.common.Utils
import com.lee.foodi.data.repository.FoodiRepository
import com.lee.foodi.data.rest.model.AddFoodData
import com.lee.foodi.data.rest.model.FoodInfoData
import com.lee.foodi.databinding.ActivityAddFoodBinding
import com.lee.foodi.ui.activities.add.fragments.AdditionalInfoFragment
import com.lee.foodi.ui.activities.add.fragments.NecessaryInfoFragment
import com.lee.foodi.ui.activities.add.viewmodel.AddFoodViewModel
import com.lee.foodi.ui.factory.FoodiViewModelFactory
import com.lee.foodi.ui.fragments.report.viewmodel.ReportViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "AddFoodActivity"

class AddFoodActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddFoodBinding
    private lateinit var mNecessaryInfoFragment : NecessaryInfoFragment
    private lateinit var mAdditionalInfoFragment : AdditionalInfoFragment
    private lateinit var mViewModel : AddFoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        AddFoodViewModel.newInstance(this)
        mViewModel = AddFoodViewModel.getInstance()!!
        addListeners()
        observeData()
        savedInstanceState?:let {
            mNecessaryInfoFragment = NecessaryInfoFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.contentsFragment ,  mNecessaryInfoFragment).commit()
        }
    }

    /**
     * Function for add listeners
     * **/
    private fun addListeners() {
        with(binding){
            confirmButton.setOnClickListener {
                if(mViewModel.progress.value == 1){
                    if(!::mAdditionalInfoFragment.isInitialized){
                        mAdditionalInfoFragment = AdditionalInfoFragment.newInstance()
                    }
                    supportFragmentManager.beginTransaction().replace(R.id.contentsFragment , mAdditionalInfoFragment).commit()
                    mViewModel.progress.value = 2
                    mViewModel.headTitle.value = resources.getString(R.string.next_add_food_header_title)
                    mViewModel.buttonText.value = resources.getString(R.string.confirm)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val addFoodData : AddFoodData
                        with(mViewModel){
                            addFoodData = AddFoodData(
                                foodName.value?.toString() ?: NOT_AVAILABLE ,
                                servingSize.value?.toString() ?: NOT_AVAILABLE ,
                                calorie.value?.toString() ?: NOT_AVAILABLE ,
                                carbohydrate.value?.toString() ?: NOT_AVAILABLE ,
                                protein.value?.toString() ?: NOT_AVAILABLE ,
                                fat.value?.toString() ?: NOT_AVAILABLE ,
                                sugar.value?.toString() ?: NOT_AVAILABLE ,
                                salt.value?.toString() ?: NOT_AVAILABLE ,
                                cholesterol.value?.toString() ?: NOT_AVAILABLE ,
                                saturatedFat.value?.toString() ?: NOT_AVAILABLE ,
                                transFat.value?.toString() ?: NOT_AVAILABLE ,
                                companyName.value?.toString() ?: NOT_AVAILABLE ,
                            )
                        }
                        Log.d(TAG, "addListeners: $addFoodData")
                        mViewModel.postRequestAddFood(addFoodData)
                    }
                }
            }

            backButton.setOnClickListener{
                if(addProgressBar.progress == 1){
                    finish()
                } else {
                    if(!::mNecessaryInfoFragment.isInitialized){
                        mNecessaryInfoFragment = NecessaryInfoFragment.newInstance()
                    }
                    supportFragmentManager.beginTransaction().replace(R.id.contentsFragment , mNecessaryInfoFragment).commit()
                    mViewModel.progress.value = 1
                    mViewModel.headTitle.value = resources.getString(R.string.add_food_header_title)
                    mViewModel.buttonText.value = resources.getString(R.string.next)
                }
            }
        }
    }

    /**
     * Function for Observe LiveData
     * **/
    private fun observeData() {
        with(mViewModel){
            progress.observe(this@AddFoodActivity){
                binding.addProgressBar.progress = it
            }

            headTitle.observe(this@AddFoodActivity){
                binding.headerTitle.text = it
            }

            buttonText.observe(this@AddFoodActivity){
                binding.confirmButton.text = it
            }

            errorMessage.observe(this@AddFoodActivity){
                Utils.toastMessage(it)
            }

            isProgressShowing.observe(this@AddFoodActivity){
                if(it){
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}