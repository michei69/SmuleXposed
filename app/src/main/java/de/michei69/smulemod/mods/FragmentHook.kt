package de.michei69.smulemod.mods

import android.annotation.SuppressLint
import android.app.ActionBar
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.paris.Paris
import de.michei69.smulemod.UI.DSTextView
import de.michei69.smulemod.UI.Resources
import de.michei69.smulemod.util.Logging
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import java.lang.Exception

typealias FragmentListener = (type: String, layoutInflater: LayoutInflater?, viewGroup: ViewGroup, bundle: Bundle?, fragment: Any?) -> Unit

class FragmentHook(classLoader: ClassLoader) {
    private var listeners = mutableListOf<FragmentListener>()

    companion object {
        private lateinit var INSTANCE: FragmentHook
        fun getInstance(): FragmentHook? {
            if (::INSTANCE.isInitialized) return INSTANCE
            else return null
        }
    }

    init {
        INSTANCE = this
        hookAllFragments(classLoader)
    }

    @Synchronized
    fun addListener(listener: FragmentListener) = listeners.add(listener)
    @Synchronized
    fun removeListener(listener: FragmentListener) = listeners.remove(listener)
    @Synchronized
    fun sendEvent(type: String, layoutInflater: LayoutInflater?, viewGroup: ViewGroup, bundle: Bundle?, fragment: Any?) = listeners.forEach {
        try {
            it(type, layoutInflater, viewGroup, bundle, fragment)
        } catch (e: Exception) {
            Logging.log("[FragmentHook] Error in listener: ${e.message}")
        }
    }

    fun hookAllFragments(classLoader: ClassLoader) {
        Logging.log("[FragmentHook] Hooking all fragments...")
        hookFragment(classLoader, "com.smule.singandroid.SettingsFragment")
    }

    fun hookFragment(classLoader: ClassLoader, className: String) {
        XposedHelpers.findAndHookMethod(
            className,
            classLoader,
            "onCreateView",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Bundle::class.java,
            object : XC_MethodHook() {
                @SuppressLint("ResourceType")
                override fun afterHookedMethod(param: MethodHookParam) {
                    val rootView = param.result as ViewGroup
                    val inflater = param.args[0] as LayoutInflater?
                    val bundle = param.args[2] as Bundle?
                    sendEvent(
                        className.split(".").last(),
                        inflater,
                        rootView,
                        bundle,
                        param.thisObject
                    )
                }
            }
        )
    }
}