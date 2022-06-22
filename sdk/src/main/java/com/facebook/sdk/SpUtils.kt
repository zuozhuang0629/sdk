package com.facebook.sdk

import android.content.Context

object SpUtils {

    fun saveUser(context: Context, value: String) {
        val sp = context.getSharedPreferences("facebookSdk", Context.MODE_PRIVATE)
        val edit = sp.edit()
        edit.putString("saveUser", value)
        edit.apply()
    }

    fun savePage(context: Context, value: String) {
        val sp = context.getSharedPreferences("facebookSdk", Context.MODE_PRIVATE)
        val edit = sp.edit()
        edit.putString("savePage", value)
        edit.apply()
    }

    fun getUser(context: Context): String {
        val sp = context.getSharedPreferences("facebookSdk", Context.MODE_PRIVATE)
        return sp.getString("saveUser", "") ?: ""

    }

    fun getPage(context: Context): String {
        val sp = context.getSharedPreferences("facebookSdk", Context.MODE_PRIVATE)
        return sp.getString("savePage", "") ?: ""
    }
}