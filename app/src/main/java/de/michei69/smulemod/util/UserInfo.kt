package de.michei69.smulemod.util

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object UserInfo {
    var accountId: Long = 0

    fun hookLoginInfo(classLoader: ClassLoader) {
        val networkResponse = classLoader.loadClass("com.smule.android.network.core.NetworkResponse")
        val jsonNode = classLoader.loadClass("com.fasterxml.jackson.databind.JsonNode")

        val getLongFromJson = networkResponse.declaredMethods.first { method ->
            method.parameterCount == 3 &&
            method.parameterTypes[0].equals(jsonNode) &&
            method.parameterTypes[1].equals(String::class.java) &&
            method.parameterTypes[2].equals(Long::class.java) &&
            method.returnType.equals(Long::class.java)
        }
        XposedBridge.hookMethod(getLongFromJson, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                if (param.args[1].equals("accountId")) {
                    accountId = param.result as Long
                    Logging.log("[LOGIN] Logged in with ${accountId}")
                }
            }
        })
    }
}