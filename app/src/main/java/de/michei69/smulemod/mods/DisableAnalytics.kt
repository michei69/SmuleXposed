package de.michei69.smulemod.mods

import de.michei69.smulemod.util.AppCompatUtility
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers.callMethod

/**
 * Disables all smule analytics and telemetry
 * (Saves internet and makes the app faster)
 */
object DisableAnalytics {
    fun hookAnalytics(classLoader: ClassLoader) {
        val eventLogger = classLoader.loadClass("com.smule.android.logging.EventLogger2")
        val createMethod = eventLogger.declaredMethods.first { method ->
            method.parameterCount == 0 &&
            method.returnType.equals(eventLogger)
        }

        XposedBridge.hookMethod(createMethod, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                if (AppCompatUtility.sharedPrefs != null && !AppCompatUtility.sharedPrefs.getBoolean("disable_analytics", true)) return
                val instance = param.result
                val sendMethod = instance.javaClass.declaredMethods.first { method ->
                    method.parameterCount == 1 &&
                    method.parameterTypes[0].equals(List::class.java) &&
                    method.returnType.equals(Boolean::class.java)
                }
                sendMethod.isAccessible = true

                //* disable the send method for all event loggers created
                XposedBridge.hookMethod(sendMethod, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        param.result = true
                    }
                })

                //* delete the event logger file, if any
                val contextField = instance.javaClass.declaredFields.first { field ->
                    field.type.equals(classLoader.loadClass("android.content.Context"))
                }
                contextField.isAccessible = true
                callMethod(contextField.get(instance), "deleteFile", "event-logger-2")
            }
        })
    }
}