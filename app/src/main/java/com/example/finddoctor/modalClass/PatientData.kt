package com.example.finddoctor.modalClass

import android.content.Context

object PatientData {

    private const val PREFS_NAME = "FindDoctorPrefs"

    fun saveLoginStatus(context: Context, value: Boolean) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("PATIENT_LOGIN_STATUS", value).apply()
    }

    fun getLoginStatus(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean("PATIENT_LOGIN_STATUS", false)
    }

    fun saveUserMail(context: Context, name: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("USER_GMAIL", name).apply()
    }

    fun getUserMail(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString("USER_GMAIL", null)
    }
}