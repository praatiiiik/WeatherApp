package com.example.weatherapp.prefHelper

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.atomic.AtomicBoolean

class PreferenceHelper(context: Context) {
    private val mPrefs: SharedPreferences = context.getSharedPreferences(mPrefFileName, Context.MODE_PRIVATE)

    private val mEditor: SharedPreferences.Editor = mPrefs.edit()

    fun getLong(key: String): Long {
        return mPrefs.getLong(key, 0)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return mPrefs.getLong(key, defaultValue)
    }

    fun setLong(key: String, value: Long) {
        mEditor.putLong(key, value)
        mEditor.apply()
    }

    fun getDouble(key: String): Double {
        return java.lang.Double.longBitsToDouble(getLong(key))
    }

    fun setDouble(key: String, value: Double) {
        mEditor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
        mEditor.apply()
    }

    fun setInt(key: String, value: Int) {
        mEditor.putInt(key, value)
        mEditor.apply()
    }

    fun getInt(key: String): Int {
//        val encryptedValue = mPrefs.getString(encrypt(key), encrypt("0"))
//        return decrypt(encryptedValue)?.toInt()
        return mPrefs.getInt(key, 0)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return mPrefs.getInt(key, defaultValue)
    }

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

    fun getString(key: String, value: String): String {
        return mPrefs.getString(key, value) ?: ""
    }

    fun getObjectInString(key: String): String {
        return mPrefs.getString(key, "") ?: ""
    }

    fun removePrefValue(key: String) {
        mEditor.remove(key).commit()
    }

    fun setFloat(key: String, value: Float) {
        mEditor.putFloat(key, value)
        mEditor.apply()
    }

    fun getFloat(key: String): Float {
        return mPrefs.getFloat(key, 0f)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mPrefs.getBoolean(key, defaultValue)
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