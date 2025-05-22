package de.michei69.smulemod.mods

import de.michei69.smulemod.util.Logging
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers.callMethod

/**
 * Hooks the JSON utils parsing
 */
object JSONHook {
    fun runPatch(classLoader: ClassLoader) {
        val jsonNode = classLoader.loadClass("com.fasterxml.jackson.databind.JsonNode")
        val jsonUtils = classLoader.loadClass("com.smule.android.utils.JsonUtils")
        val getNodeAsClass = jsonUtils.declaredMethods.first { method ->
            method.parameterCount == 3 &&
            method.parameterTypes[0].equals(jsonNode) &&
            method.parameterTypes[1].equals(Class::class.java) &&
            method.parameterTypes[2].equals(Boolean::class.java) &&
            method.returnType.equals(Object::class.java)
        }

        XposedBridge.hookMethod(getNodeAsClass, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val cls = param.args[1]
                val clsName = callMethod(cls, "getSimpleName") as String
                ModelCreationHooks.getInstance()!!.sendEvent(clsName, param.result)
                Logging.log("[JSONUtils] $clsName")
            }
        })
    }
}