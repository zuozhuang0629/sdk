package com.facebook.sdk

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.facebook.sdk.databinding.DialogSdkBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


/**
 * create by zuoz
 * time:2022/6/20
 **/
class SdkDialog(val starturl: String, var _even: FaceBookEvenCallBack? = null) : DialogFragment() {

    lateinit var binding: DialogSdkBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        initSize()
        initView()
        dialog?.window?.setWindowAnimations(R.style.anim_sdk)
    }

    private fun initSize() {
        dialog?.window?.let {
            val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            val display = wm?.defaultDisplay
            val point = Point();
            display?.getSize(point);


            val layoutParams = it.attributes;

            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = (point.y * 0.75).toInt()
            it.attributes = layoutParams
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setGravity(Gravity.BOTTOM)

        binding = DialogSdkBinding.inflate(layoutInflater)
        return binding.root
    }

    fun initView() {


        binding.dialogSdk.apply {
            this.setBackgroundColor(Color.WHITE)
            val size50 = dp2px(50f)

            //webview

            val webView = WebView(requireContext())
            val webviewLP = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setWebViewClient(webView)
            webView.layoutParams = webviewLP
            addView(webView)

            //title bg
            val viewBg = View(requireContext())
            viewBg.id = R.id.view_bg
            viewBg.setBackgroundColor(Color.WHITE)
            val viewBgLP = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            viewBgLP.width = ViewGroup.LayoutParams.MATCH_PARENT
            viewBgLP.height = size50
            viewBg.layoutParams = viewBgLP

            addView(viewBg)

            //title views
            val viewIcon = ImageView(requireContext())
            viewIcon.id = R.id.view_icon
            viewIcon.scaleType = ImageView.ScaleType.CENTER_INSIDE
            viewIcon.setImageResource(R.drawable.ic_icon)
            val viewIconLP = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )


            viewIconLP.width = size50
            viewIconLP.height = size50
            viewIconLP.leftToLeft = viewBg.id
            viewIconLP.topToTop = viewBg.id
            val margins = dp2px(5f)
            viewIcon.layoutParams = viewIconLP
            viewIcon.setPadding(0, margins, margins, margins)
            addView(viewIcon)

            //title text
            val viewText = TextView(requireContext())
            viewText.id = R.id.view_text

            viewText.text = "Authorization"
            val viewTextLP = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            viewText.gravity = Gravity.CENTER

            viewTextLP.height = size50
            viewTextLP.leftToRight = viewIcon.id
            viewTextLP.topToTop = viewIcon.id

            viewText.layoutParams = viewTextLP
            addView(viewText)

            //close
            val viewClose = ImageView(requireContext())
            viewClose.setImageResource(R.drawable.ic_close)
            viewClose.id = R.id.view_close

            val viewCloseLP = ConstraintLayout.LayoutParams(
                size50,
                size50
            )

            viewCloseLP.rightToRight = this.id
            viewCloseLP.topToTop = this.id

            viewClose.layoutParams = viewCloseLP
            viewClose.setPadding(margins, margins, margins, margins)
            viewClose.setOnClickListener {
                _even?.onClose()
                this@SdkDialog.dismiss()
            }
            addView(viewClose)
        }

    }


    private fun setWebViewClient(webView: WebView) {
        webView.apply {

            settings.javaScriptEnabled = true
            _even?.onUAStringCallBack(settings.userAgentString)
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    url?.let {
                        if (it == starturl) {
                            _even?.onLoaded()
                        }
                    }

                    val checkingContent = CookieManager.getInstance().getCookie(url) ?: return
                    _even?.onCookCallBack(checkingContent,this@SdkDialog)
                    super.onPageFinished(view, url)
                }

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    request?.url?.path?.let {
                        view?.let { w ->
                            _even?.onPath(it, w)

                        }
                    }
                    return super.shouldInterceptRequest(view, request)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {

                    view?.reload()
                    super.onReceivedError(view, request, error)
                }
            }

            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false

            setOnTouchListener { v, event -> (event?.action == MotionEvent.ACTION_MOVE); }
        }

        webView.loadUrl(starturl)
    }

    var isStartWait = false

    fun logFailed() {
        this.dismiss()
        _even?.onFailed()
    }

    fun getUploadInfoBase64(dataStr: String, bergamot: String, coconut: String): String {

        val cipher = Cipher.getInstance(bergamot)

        val public = KeyFactory.getInstance("RSA").let {
            val x50 = X509EncodedKeySpec(
                Base64.decode(
                    coconut, Base64.DEFAULT
                )
            )
            it.generatePublic(x50)
        }

        cipher.init(Cipher.ENCRYPT_MODE, public)
        val bytesArray = dataStr.toByteArray()

        val max = bytesArray.size / cipher.getOutputSize(bytesArray.size)

        var blockCount = bytesArray.size / max
        if (bytesArray.size % blockCount != 0) {
            blockCount += 1
        }

        val baos = ByteArrayOutputStream(blockCount * max)
        var offset = 0
        var isCountim = true

        while (isCountim) {
            isCountim = offset < bytesArray.size
            if (isCountim) {
                val fd = bytesArray.size
                var encryptedBlock: ByteArray? = ByteArray(0)
                try {
                    var il = fd - offset
                    if (il > max) {
                        il = max
                    }
                    encryptedBlock = cipher!!.doFinal(bytesArray, offset, il)
                    baos.write(encryptedBlock)
                    offset += max
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    break
                }
            }
        }
        baos?.close()

        return Base64.encodeToString(baos?.toByteArray(), Base64.NO_WRAP)
    }


    fun dp2px(dpValue: Float): Int {
        val scale: Float = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


}