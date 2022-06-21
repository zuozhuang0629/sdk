package com.facebook.sdk

interface EvenCallBack {
    fun onCloseListener()
    fun onResultListener()
    fun onUAStringCallBack(ua: String)
    fun onLoaded()
}