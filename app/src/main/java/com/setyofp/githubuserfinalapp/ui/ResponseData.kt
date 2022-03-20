package com.setyofp.githubuserfinalapp.ui

sealed class ResponseData<T>(val data: T?  = null, val message: String? = null) {
    class Success<T>(data: T?): ResponseData<T>(data)
    class Error<T>(message: String?, data: T? = null): ResponseData<T>(data, message)
    class Loading<T>(data: T? = null): ResponseData<T>(data)
}

