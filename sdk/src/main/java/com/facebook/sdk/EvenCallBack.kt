package com.facebook.sdk

interface EvenCallBack {
    fun onSuccess()
    fun onUAStringCallBack(ua: String)
    fun onLoaded()
    fun onFailed()
}