package com.lee.foodi.common.manager

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * InConsistent Exception 발생으로 인한 Custom LinearLayoutManager
 * **/
class CustomLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}