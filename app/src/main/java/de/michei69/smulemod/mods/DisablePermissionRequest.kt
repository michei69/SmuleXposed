package de.michei69.smulemod.mods

import de.michei69.smulemod.util.AppCompatUtility
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object DisablePermissionRequest {
    fun runPatch(classLoader: ClassLoader) {
        val thing = classLoader.loadClass("com.smule.singandroid.runtimepermissions.RegularLaunchSingPermissionRequestsUseCase")
        val method = thing.declaredMethods.first { method ->
            method.parameterCount == 2
        }

        XposedBridge.hookMethod(method, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (AppCompatUtility.sharedPrefs != null && !AppCompatUtility.sharedPrefs.getBoolean("disable_loc_req", true)) return
                param.result = null
            }
        })
    }
}