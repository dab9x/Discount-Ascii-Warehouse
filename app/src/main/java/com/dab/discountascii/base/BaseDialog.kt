package com.dab.discountascii.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import com.dab.discountascii.R

abstract class BaseDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val coverLayout = FrameLayout(context)
            .apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(
                    horizontalPadding(),
                    verticalPadding(),
                    horizontalPadding(),
                    verticalPadding()
                )
            }

        setupView(savedInstanceState, coverLayout)

        setContentView(coverLayout)

        setCancelable(isCancelable())

        window?.let {
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    abstract fun setupView(savedInstanceState: Bundle?, coverLayout: FrameLayout)

    protected open fun isCancelable(): Boolean = false

    protected open fun horizontalPadding(): Int =
        context.resources.getDimensionPixelSize(R.dimen.dimen_10dp)

    protected open fun verticalPadding(): Int = 0

    abstract fun getLayoutResId(): Int
}