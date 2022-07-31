package com.example.mycart.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefHelper {

    private var SHARED_PREF_INSTANCE: SharedPreferences? = null

    fun getSharedPrefInstance(context: Context): SharedPreferences {
        if (SHARED_PREF_INSTANCE == null) {
            SHARED_PREF_INSTANCE =
                context.getSharedPreferences(
                    Constants.SHARED_PREFERENCES_DATABASE,
                    Context.MODE_PRIVATE
                )
        }
        return SHARED_PREF_INSTANCE!!
    }

    fun saveUserName(key: String, name: String) {
        val editor = SHARED_PREF_INSTANCE!!.edit()
        editor.apply {
            putString(key, name)
            apply()
        }
    }

    fun getUserName(key: String): String {
        return SHARED_PREF_INSTANCE!!.getString(key, "")!!
    }

}