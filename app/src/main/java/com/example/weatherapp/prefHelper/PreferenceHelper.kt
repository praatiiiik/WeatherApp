package com.example.weatherapp.prefHelper

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Shared Pref used to store key value pair
 * Creating Synchronized and singleton object of shared preference
 */
class PreferenceHelper(context: Context) {
    private val mPrefs: SharedPreferences = context.getSharedPreferences(mPrefFileName, Context.MODE_PRIVATE)

    private val mEditor: SharedPreferences.Editor = mPrefs.edit()

    fun setBoolean(key: String, value: Boolean) {
        mEditor.putBoolean(key, value)
        mEditor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return mPrefs.getBoolean(key, false)
    }

    fun setString(key: String, value: String) {
        mEditor.putString(key, value)
        mEditor.apply()
    }

    fun getString(key: String): String {
        return mPrefs.getString(key, "") ?: ""
    }


    companion object {
        private var mPrefFileName = PrefConstants.PREFERENCE_NAME
        private lateinit var INSTANCE: PreferenceHelper
        private val initialized = AtomicBoolean()

        @Synchronized
        fun getInstance(context: Context): PreferenceHelper {
            if (!initialized.getAndSet(true)) {
                INSTANCE = PreferenceHelper(context)
            }
            return INSTANCE
        }
    }
}