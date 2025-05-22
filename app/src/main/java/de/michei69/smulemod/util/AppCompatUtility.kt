package de.michei69.smulemod.util

import android.content.Context
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.callMethod
import de.robv.android.xposed.XposedHelpers.findAndHookConstructor


object AppCompatUtility {
    var appCompatActivity: Any? = null // extended appcompat activity
    val supportFragmentManager
        get() = callMethod(appCompatActivity!!, "getSupportFragmentManager")
    val context
        get() = callMethod(appCompatActivity!!, "getApplicationContext") as Context
    val sharedPrefs
        get() = context.getSharedPreferences("SMULE_MOD", Context.MODE_PRIVATE)

    fun hookAppCompat(classLoader: ClassLoader) {
        findAndHookConstructor(
            "androidx.appcompat.app.AppCompatActivity",
            classLoader,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    appCompatActivity = param.thisObject as Any
                }
            }
        )
    }
}