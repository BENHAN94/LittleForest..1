package com.benhan.bluegreen

import android.content.Context
import android.content.SharedPreferences

class SharedPreference {

    companion object{

        const val PREFERENCE_NAME = "user"
        const val DEFAULT_VALUE_STRING = ""
        const val DEFAULT_VALUE_BOOLEAN = false

    }

    private fun getPreference(context: Context): SharedPreferences {


        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)


    }


    fun setString(context: Context, key: String, value: String){

        val prefs = getPreference(context)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()

    }

    fun setBoolean(context: Context, key: String, value: Boolean){

        val prefs = getPreference(context)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()

    }

    fun getString(context: Context, key: String): String?{

        val prefs = getPreference(context)
        val value = prefs.getString(key, DEFAULT_VALUE_STRING)
        return value

    }


    fun getBoolean(context: Context, key: String): Boolean?{

        val prefs = getPreference(context)
        val value = prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN)
        return value

    }

    fun removeKey(context: Context, key: String){

        val prefs = getPreference(context)
        val edit = prefs.edit()
        edit.remove(key)
        edit.apply()

    }

    fun clear(context: Context){

        val prefs = getPreference(context)
        val edit = prefs.edit()
        edit.clear()
        edit.apply()
    }

}