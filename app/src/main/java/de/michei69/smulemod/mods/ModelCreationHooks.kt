package de.michei69.smulemod.mods

import android.os.Parcel
import de.michei69.smulemod.util.Logging
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookConstructor
import java.lang.Exception

typealias Listener = (type: String, data: Any) -> Unit

/**
 * Hooks all model creations (AccountIcon, PerformanceV2, etc)
 *
 * Other mods can listen for creations, and modify the models respectively
 */
class ModelCreationHooks(classLoader: ClassLoader) {
    private var listeners = mutableListOf<Listener>()

    companion object {
        private lateinit var INSTANCE: ModelCreationHooks
        fun getInstance(): ModelCreationHooks? {
            if (::INSTANCE.isInitialized) return INSTANCE
            else return null
        }
    }

    init {
        INSTANCE = this
        hookAllModels(classLoader)
    }

    @Synchronized
    fun addListener(listener: Listener) = listeners.add(listener)
    @Synchronized
    fun removeListener(listener: Listener) = listeners.remove(listener)
    @Synchronized
    fun sendEvent(type: String, data: Any) = listeners.forEach {
        try {
            it(type, data)
        } catch (e: Exception) {
            Logging.log("[ModelCreationHooks] Error in listener: ${e.message}")
        }
    }

    fun hookAllModels(classLoader: ClassLoader) {
        Logging.log("[ModelCreationHooks] Hooking all models...")
        hookAccountIcon(classLoader)
        hookArrangement(classLoader)
        hookOpenCallV2(classLoader)
        hookPerformanceV2(classLoader)
        hookSingUserProfile(classLoader)
    }

    fun hookAccountIcon(classLoader: ClassLoader) {
        findAndHookConstructor(
            "com.smule.android.network.models.AccountIcon",
            classLoader,
            Parcel::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    sendEvent("AccountIcon", param.thisObject)
                }
            }
        )
    }

    fun hookArrangement(classLoader: ClassLoader) {
        findAndHookConstructor(
            "com.smule.android.network.models.Arrangement",
            classLoader,
            Parcel::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    sendEvent("Arrangement", param.thisObject)
                }
            }
        )
    }

    fun hookOpenCallV2(classLoader: ClassLoader) {
        findAndHookConstructor(
            "com.smule.android.network.models.OpenCallV2",
            classLoader,
            Parcel::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    sendEvent("OpenCallV2", param.thisObject)
                }
            }
        )
    }

    fun hookPerformanceV2(classLoader: ClassLoader) {
        findAndHookConstructor(
            "com.smule.android.network.models.PerformanceV2",
            classLoader,
            Parcel::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    sendEvent("PerformanceV2", param.thisObject)
                }
            }
        )
    }

    fun hookSingUserProfile(classLoader: ClassLoader) {
        val singUserProfile = classLoader.loadClass("com.smule.android.network.models.SingUserProfile")
        val singUserManager = classLoader.loadClass("com.smule.android.network.managers.SingUserManager")
        val getProfileMethod = singUserManager.declaredMethods.first { method ->
            method.parameterCount == 2 &&
                    method.parameterTypes[0].equals(Long::class.java) &&
                    method.parameterTypes[1].equals(Boolean::class.java) &&
                    method.returnType.equals(singUserProfile)
        }

        XposedBridge.hookMethod(getProfileMethod, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                sendEvent("SingUserProfile", param.result)
            }
        })
    }
}