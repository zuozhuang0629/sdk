package com.facebook.sdk

interface EvenCallBack {
    fun onCloseListener()
    fun onSuccess()
    fun onUAStringCallBack(ua: String)
    fun onLoaded()
    fun onFailed()
}