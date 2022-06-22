package com.facebook.sdk

object SdkHelper {

    fun createDialog(data: String, even: EvenCallBack? = null): SdkDialog {
        return SdkDialog(data, even)
    }

    interface EvenCallBack {
        fun onSuccess()
        fun onUAStringCallBack(ua: String)
        fun onLoaded()
        fun onFailed()
    }
}