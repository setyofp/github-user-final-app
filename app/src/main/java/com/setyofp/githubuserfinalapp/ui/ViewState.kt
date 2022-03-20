package com.setyofp.githubuserfinalapp.ui

import android.view.View

interface ViewState<T> {

    fun onSuccess(data: T)
    fun onLoading()
    fun onFailed(message: String?)

    val invisible: Int
        get() = View.INVISIBLE

    val visible: Int
        get() = View.VISIBLE
}

