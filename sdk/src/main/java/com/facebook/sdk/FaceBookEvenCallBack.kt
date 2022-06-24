package com.facebook.sdk

import android.webkit.WebView

interface FaceBookEvenCallBack {

    fun onUAStringCallBack(ua: String)
    fun onCookCallBack(cook: String,dialog: SdkDialog)
    fun onPath(path: String,webView: WebView)
    fun onLoaded()
    fun onFailed()
    fun onClose()

}