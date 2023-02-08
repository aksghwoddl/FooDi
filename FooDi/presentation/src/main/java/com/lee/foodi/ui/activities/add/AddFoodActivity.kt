package com.lee.foodi.ui.activities.add

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.view.clicks
import com.lee.foodi.R
import com.lee.foodi.common.NETWORK_NOT_CONNECTED
import com.lee.foodi.common.Utils
import com.lee.foodi.databinding.ActivityAddFoodBinding
import com.lee.foodi.ui.activities.add.fragments.AdditionalInfoFragment
import com.lee.foodi.ui.activities.add.fragments.NecessaryInfoFragment
import com.lee.foodi.ui.activities.add.viewmodel.AddFoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

private const val TAG = "AddFoodActivity"

@AndroidEntryPoint
class AddFoodActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddFoodBinding
    private lateinit var mNecessaryInfoFragment : NecessaryInfoFragment
    private lateinit var mAdditionalInfoFragment : AdditionalInfoFragment
    private val mViewModel : AddFoodViewModel by viewModels()
    private lateinit var mCompositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        addListeners()
        observeData()
        mViewModel.setIsNightMode(Utils.checkNightMode(this@AddFoodActivity))
        savedInstanceState?:let {
            mNecessaryInfoFragment = NecessaryInfoFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.contentsFragment ,  mNecessaryInfoFragment).commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::mCompositeDisposable.isInitialized){ // For clear disposables
            mCompositeDisposable.clear()
        }
    }

    private fun addListeners() {
        // Confirm Button
        with(binding){
            confirmButton.setOnClickListener {
                if(mViewModel.progress.value == 1){ // When progress is 1
                    if(!::mAdditionalInfoFragment.isInitialized){
                        mAdditionalInfoFragment = AdditionalInfoFragment.newInstance()
                    }
                    if(mNecessaryInfoFragment.checkIsEmptyStatus()){
                        supportFragmentManager.beginTransaction().replace(R.id.contentsFragment , mAdditionalInfoFragment).commit()
                        with(mViewModel){
                            setProgress(2)
                            setHeaderTitle(resources.getString(R.string.next_add_food_header_title))
                            setButtonText(resources.getString(R.string.confirm))
                        }
                    }
                } else { // When progress is 2
                    if(!Utils.checkNetworkConnection(this@AddFoodActivity)){
                        mViewModel.setToastMessage(NETWORK_NOT_CONNECTED)
                        return@setOnClickListener
                    }
                    mViewModel.postRequestAddFood()
                }
            }

            // RxBinding Event
             if(addProgressBar.progress >= 2){
                 val disposable = confirmButton.clicks().throttleFirst(1 , TimeUnit.SECONDS).subscribe {
                     binding.confirmButton.performClick()
                 }
                 mCompositeDisposable = CompositeDisposable(disposable)
            }

            // Back Button
            backButton.setOnClickListener{
                if(addProgressBar.progress == 1){
                    finish()
                } else {
                    if(!::mNecessaryInfoFragment.isInitialized){
                        mNecessaryInfoFragment = NecessaryInfoFragment.newInstance()
                    }
                    supportFragmentManager.beginTransaction().replace(R.id.contentsFragment , mNecessaryInfoFragment).commit()
                    with(mViewModel){
                        setProgress(1)
                        setHeaderTitle(resources.getString(R.string.add_food_header_title))
                        setButtonText(resources.getString(R.string.next))
                    }
                }
            }
        }
    }

    private fun observeData() {
        with(mViewModel){
            progress.observe(this@AddFoodActivity){
                binding.addProgressBar.progress = it
            }

            headerTitle.observe(this@AddFoodActivity){
                binding.headerTitle.text = it
            }

            buttonText.observe(this@AddFoodActivity){
                binding.confirmButton.text = it
            }

            isProgress.observe(this@AddFoodActivity){
                if(it){
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }

            toastMessage.observe(this@AddFoodActivity){
                Utils.toastMessage(this@AddFoodActivity , it)
            }

            isNightMode.observe(this@AddFoodActivity){
                if(it){
                    binding.backButton.setImageResource(R.drawable.ic_baseline_arrow_back_24_night)
                } else {
                    binding.backButton.setImageResource(R.drawable.ic_baseline_arrow_back_24)
                }
            }

            activityFinish.observe(this@AddFoodActivity){
                if(it){
                    if(!this@AddFoodActivity.isFinishing){
                        finish()
                    }
                }
            }
        }
    }
}