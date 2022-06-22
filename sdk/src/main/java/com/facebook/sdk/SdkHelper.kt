package com.facebook.sdk

object SdkHelper {

    fun createDialog(data: String, even: EvenCallBack? = null): SdkDialog {
        return SdkDialog(data, even)
    }
}